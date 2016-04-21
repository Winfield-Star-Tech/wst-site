package org.wst.shipbuilder;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Configuration
public class DefaultPageController {
	@RequestMapping("/")
	public String greeting(Model model) {
		return "index";
	}

}
