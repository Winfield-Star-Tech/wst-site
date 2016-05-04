/**
 * 
 */
package org.wst.shipbuilder.eveapi;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.wst.shipbuilder.data.EveCharacter;
import org.xml.sax.SAXException;

/**
 * @author John
 *
 */
public class DataDownloadTest {

	@Test
	public void testCharDownload() throws ParserConfigurationException, SAXException, IOException {
		CacheUpdater upd = new CacheUpdater();
		List<EveCharacter> downloadedChars = new ArrayList<EveCharacter>();
		
		InputStream stream = DataDownloadTest.class.getResourceAsStream("MemberTracking.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(stream);
		upd.parseMemberTracking(doc, downloadedChars);
		
		Assert.assertTrue(downloadedChars.size() > 50);
	}

	@Test
	public void testZKillboardDownload() {
		CacheUpdater upd = new CacheUpdater();
		ZKillBoardCharEntry zkbEntry = upd.getKillboardForChar(1950740146);
		Assert.assertEquals("Kalfar", zkbEntry.getName());
	}
}
