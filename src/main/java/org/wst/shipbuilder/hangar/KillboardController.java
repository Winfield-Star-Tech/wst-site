package org.wst.shipbuilder.hangar;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wst.shipbuilder.data.CharacterRepository;
import org.wst.shipbuilder.data.EveCharacter;
import org.wst.shipbuilder.data.EveUserRepository;

@RestController
@RequestMapping("/rest/hangar/user/killboard")
public class KillboardController {
	
	@Autowired
	private CharacterRepository characterRepo;
	@RequestMapping(method = RequestMethod.GET)
	List<EveCharacter> getTopKillboardEntries() {
		
		List<EveCharacter> stats = new ArrayList();
		Iterable<EveCharacter> it = characterRepo.findAll(new Sort(Sort.Direction.DESC, "shipDestroyedBalance"));
		for(EveCharacter e : it) {
			stats.add(e);
		}
		//List<EveCharacter> stats = characterRepo.findAllOrderByShipDestroyedBalanceDesc(new PageRequest(1, 20));
		
		return stats.subList(0,  Math.min(20, stats.size()-1));
	}

}
