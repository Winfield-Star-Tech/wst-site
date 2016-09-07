package org.wst.shipbuilder.data.fittings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class CorpFitService {
	private static final Logger log = LoggerFactory.getLogger(CorpFitService.class);
	private Map<String,ShipFitting> fittings;
	public CorpFitService () throws JAXBException, IOException {
		
			fittings = shipFittingMap();
		
	}
	
	Map<String,ShipFitting> shipFittingMap() throws JAXBException, IOException {
		Map<String, ShipFitting> m = new HashMap<String, ShipFitting>();
		JAXBContext jaxbContext = JAXBContext.newInstance(Fittings.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	     
	    //We had written this file in marshalling example
	    Resource resource = new ClassPathResource("corp-fits.xml");
	    InputStream resourceInputStream = resource.getInputStream();
	    Fittings fittings = (Fittings) jaxbUnmarshaller.unmarshal( resourceInputStream) ;
	    log.info("Adding " + fittings.getFittings().size() + " fittings to bean");
	    for(ShipFitting f: fittings.getFittings()) {
	    	
	    	m.put(f.getName(), f);
	    }
	    return m;
	}

	public ShipFitting findFitting(String name) {
		log.info("Looking up fit" + name);
		ShipFitting f = fittings.get(name);
		if(f == null) {
			throw new FittingNotFoundException();
		}
		return f;
	}

	public Map<String, ShipFitting> getShippingsMap() {		
		return fittings;
	}
}
