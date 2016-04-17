package org.wst.shipbuilder;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wst.shipbuilder.data.EveUser;
import org.wst.shipbuilder.data.EveUserRepository;

@RestController
@RequestMapping("/rest/users")
public class AdminRestController {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private EveUserRepository userRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	Iterable<EveUser> getAll() {
		return userRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<Iterable<EveUser>> saveAll(@RequestBody Collection<EveUser> users) {
		logger.info("Saving all");
		for(EveUser user: users) {
			logger.info("Saving user" + user.getCharacterName());
			userRepository.save(user);
		}
		 return new ResponseEntity<Iterable<EveUser>>(userRepository.findAll(), HttpStatus.OK);
	}
	
}
