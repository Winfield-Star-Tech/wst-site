package org.wst.shipbuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EveUserInfoTokenServices  implements ResourceServerTokenServices {

	private static final String CHARACTER_NAME = "CharacterName";

	private static final String CHARACTER_ID = "CharacterID";

	protected final Log logger = LogFactory.getLog(getClass());

	private static final String[] PRINCIPAL_KEYS = new String[] { "user", "username",
			"userid", "user_id", "login", "id", "name", CHARACTER_NAME };

	private final String userInfoEndpointUrl;

	private final String clientId;

	private OAuth2RestOperations restTemplate;

	private String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;

	private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

	public EveUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
		this.userInfoEndpointUrl = userInfoEndpointUrl;
		this.clientId = clientId;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public void setRestTemplate(OAuth2RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
		this.authoritiesExtractor = authoritiesExtractor;
	}

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
		if (map.containsKey("error")) {
			this.logger.debug("userinfo returned error: " + map.get("error"));
			throw new InvalidTokenException(accessToken);
		}
		getExtraUserInfo(map);
		lookupRoles(map);
		
		return extractAuthentication(map);
	}

	
	private void lookupRoles(Map<String, Object> map) {
		String characterName = (String)map.get(CHARACTER_NAME);
		
		if(characterName.equalsIgnoreCase("Kalfar") 
				|| characterName.equalsIgnoreCase("Toshuu Milia")
				|| characterName.equalsIgnoreCase("Cole Winfield")) {
			addRole("ROLE_ADMIN", map);			
		}		
		String corporation = (String)map.get("corporation");
		if(corporation.equalsIgnoreCase("Winfield Star-Tech")) {
			addRole("ROLE_WST_USER", map);
		}
	}
	private void addRole(String role, Map<String, Object> map) {
		String authorities = (String) map.get("authorities");
		if (authorities == null) {
			authorities = "ROLE_USER";
		} 
		authorities += ",";
		authorities += role;
		map.put("authorities", authorities);
	}

	private void getExtraUserInfo(Map<String, Object> map) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Integer characterID = (Integer)map.get(CHARACTER_ID);
		if(characterID == null) {
			logger.error("Cannot find CharacterID when looking up extra user information");
		}
		String dataURL = "https://api.eveonline.com/eve/CharacterInfo.xml.aspx?characterID=";
			
		dataURL += characterID;
		try {
			builder = factory.newDocumentBuilder();
		
		Document doc = builder.parse(dataURL);
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("/eveapi/result/*");
		NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if(!n.getNodeName().equalsIgnoreCase("rowset")) {
				
				map.put(n.getNodeName(), n.getTextContent());
				logger.info("Adding field " + n.getNodeName() + "=" + n.getTextContent());
			}
			
		}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			logger.error("Error", e);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			logger.error( "Error", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error( "Error", e);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			logger.error( "Error", e);
		}
	}
	
	

	private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
		Object principal = getPrincipal(map);
		List<GrantedAuthority> authorities = this.authoritiesExtractor
				.extractAuthorities(map);
		OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null,
				null, null, null, null);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				principal, "N/A", authorities);
		// Can get corp from here:
		// https://api.eveonline.com/eve/CharacterInfo.xml.aspx?characterID=94925238
		token.setDetails(map);
		return new EveOauth2Authentication(request, token, map);
	}

	private Object getPrincipal(Map<String, Object> map) {
		for (String key : PRINCIPAL_KEYS) {
			if (map.containsKey(key)) {
				return map.get(key);
			}
		}
		return "unknown";
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}

	@SuppressWarnings({ "unchecked" })
	private Map<String, Object> getMap(String path, String accessToken) {
		this.logger.info("Getting user info from: " + path);
		try {
			OAuth2RestOperations restTemplate = this.restTemplate;
			if (restTemplate == null) {
				BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
				resource.setClientId(this.clientId);
				restTemplate = new OAuth2RestTemplate(resource);
			}
			OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext()
					.getAccessToken();
			if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
				DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(
						accessToken);
				token.setTokenType(this.tokenType);
				restTemplate.getOAuth2ClientContext().setAccessToken(token);
			}
			return restTemplate.getForEntity(path, Map.class).getBody();
		}
		catch (Exception ex) {
			this.logger.info("Could not fetch user details: " + ex.getClass() + ", "
					+ ex.getMessage());
			return Collections.<String, Object>singletonMap("error",
					"Could not fetch user details");
		}
	}

}
