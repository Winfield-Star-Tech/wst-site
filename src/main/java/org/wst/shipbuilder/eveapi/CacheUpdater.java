package org.wst.shipbuilder.eveapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wst.shipbuilder.data.CharacterRepository;
import org.wst.shipbuilder.data.EveCharacter;
import org.xml.sax.SAXException;

@Configuration
@EnableScheduling
public class CacheUpdater {

	@Autowired
	private CharacterRepository characterRepo;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String CORP_MEMBER_TRACK_URL="https://api.eveonline.com/corp/MemberTracking.xml.aspx?keyID=%s&vCode=%s&extended=0";
	
	@Bean
	public CorpApiKey corpKey() throws NamingException {
		String keyData = (String)InitialContext.doLookup("java:global/corp-eve-key");
		String[] fields = keyData.split(",");
		
		CorpApiKey k = new CorpApiKey();
		k.setKeyid(fields[0]);
		k.setVerificationCode(fields[1]);
		return k;
	}
	// Six hour delay
	@Scheduled(initialDelay=1000, fixedRate=21600000)
	public void refreshCorpMembers() {
		logger.info("Refreshing Corp Member Cache");
		List<EveCharacter> downloadedChars = null;
		try {
			downloadedChars = downloadCorpMemberList();
			List<EveCharacter> currentChars = new ArrayList<EveCharacter>();
			for(EveCharacter c : characterRepo.findAll()) {
				currentChars.add(c);
			}
			logger.info("Database has  " + currentChars.size() + " characters");
			logger.info("Downloaded details of  " + downloadedChars.size() + " characters");
			
			
			
			List<EveCharacter> charsToRemove = new ArrayList<EveCharacter>(currentChars);
			charsToRemove.removeAll(downloadedChars);
			List<EveCharacter> charsToAdd = new ArrayList<EveCharacter>(downloadedChars);
			charsToAdd.removeAll(currentChars);
			logger.info("Removing " + charsToRemove.size() + " characters");
			logger.info("Adding " + charsToAdd.size() + " characters");
			
			characterRepo.delete(charsToRemove);
			characterRepo.save(charsToAdd);
			Iterable<EveCharacter> chars = characterRepo.findAll();
			for(EveCharacter c : chars) {
				ZKillBoardCharEntry zc = getKillboardForChar(c.getCharacterID());
				c.setShipsDestroyed(zc.getShipsDestroyed());
				c.setShipsLost(zc.getShipsLost());
				logger.info("Updated killboard for " + c.getName());
				characterRepo.save(c);
			}
		} catch (NamingException e) {
			logger.error("Error downloading characters", e);
		}
		
		
		
	}
	public List<EveCharacter> downloadCorpMemberList() throws NamingException {
		String queryString = String.format(CORP_MEMBER_TRACK_URL, corpKey().getKeyid(), corpKey().getVerificationCode());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		List<EveCharacter> downloadedChars = new ArrayList<EveCharacter>();
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(queryString);
			parseMemberTracking(doc, downloadedChars);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return downloadedChars;
	}
	public void parseMemberTracking(Document doc, List<EveCharacter> downloadedChars) {
		
		try {
			
			
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/eveapi/result/rowset/row");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				Element e = (Element)n;
				String charID = e.getAttribute("characterID");
				String charName = e.getAttribute("name");
				EveCharacter c = new EveCharacter(
				Long.parseLong(charID), charName
				);
				downloadedChars.add(c);
			}
			
		} catch (XPathExpressionException e) {
			logger.error("Error downloading character list", e);
		}
	}
	
	public ZKillBoardCharEntry getKillboardForChar(long characterID) {
		RestTemplate restTemplate = new RestTemplate();
		String url = String.format("https://zkillboard.com/api/stats/characterID/%d/", characterID);
		ZKillBoardCharEntry entry = restTemplate.getForObject(url, ZKillBoardCharEntry.class);
		return entry;
	}
}