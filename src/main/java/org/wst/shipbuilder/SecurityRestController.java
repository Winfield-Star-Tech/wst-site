package org.wst.shipbuilder;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/security")
public class SecurityRestController {

	@RequestMapping("user")
	  public Principal user(Principal principal) {
	    return principal;
	  }
	
}
