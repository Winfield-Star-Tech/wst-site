package org.wst.shipbuilder.data.fittings;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class ShipFittingRestController {

	@Autowired
	private CorpFitService fittingService;
	/*
	@RequestMapping("/rest/fitting")
	public ShipFitting fitting(@RequestParam(name="name", required=true)String name) {
		return fittingService.findFitting(name);
	}
	*/
	@RequestMapping("/rest/fitting")
	public Map<String, ShipFitting>fittings() {
		return fittingService.getShippingsMap();
	}
}
