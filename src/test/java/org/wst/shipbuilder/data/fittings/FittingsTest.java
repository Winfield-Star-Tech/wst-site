package org.wst.shipbuilder.data.fittings;

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class FittingsTest {

	@Test
	public void testLoad() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Fittings.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	     
	    //We had written this file in marshalling example
	    Fittings fittings = (Fittings) jaxbUnmarshaller.unmarshal( new File("test-corp-fits.xml") );
	    assertEquals(83, fittings.getFittings().size());
	    for(ShipFitting f : fittings.getFittings()) {
	    	assertNotNull(f.getName());
	    	assertTrue(f.getHardware().size() > 0);
	    	assertNotNull(f.getShipType());
	    	assertNotNull(f.getShipType().getValue());
	    	assertTrue(f.getShipType().getValue().trim().length() > 0);
	    	for(ShipHardware hw : f.getHardware()) {
	    		assertNotNull(hw.getHardwareType());
	    		assertNotNull(hw.getSlot());
	    		assertTrue(hw.getQuantity() >= 0);
	    	}
	    }
	}

}
