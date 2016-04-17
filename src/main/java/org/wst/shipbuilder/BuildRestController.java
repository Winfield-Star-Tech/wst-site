package org.wst.shipbuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wst.shipbuilder.data.BuildOperation;
import org.wst.shipbuilder.data.BuildOperationRepository;

@RestController
@RequestMapping("/rest/build")
public class BuildRestController {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private BuildOperationRepository buildRepo;

	@RequestMapping(method = RequestMethod.GET)
	public BuildOperation getOperation(@RequestParam(name="id", required=true) long id) {
		return buildRepo.findOne(id);
	}
	
	@RequestMapping(path="reset") 
	public void reset() {
		buildRepo.deleteAll();
		createDefaultEntry();
	}

	private void createDefaultEntry() {
		// TODO Auto-generated method stub
		
	}
}
