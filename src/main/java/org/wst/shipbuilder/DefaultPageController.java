package org.wst.shipbuilder;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Configuration
public class DefaultPageController {
	private int donations = 0;
	@RequestMapping("/")
	public String greeting(Model model) {
		return "index";
	}
	
	@RequestMapping("/toshuu")
	public String toshuu(Model model) {
		return "toshuu";
	}
	@RequestMapping("/operation-hephaestus")
	public String hephaestus(Model model) {
		model.addAttribute("donations", donations + (1E7 * Math.random()));
		return "Hephaestus";
	}
	@RequestMapping("/ship-orders")
	public String shipOrders(Model model) {
		return "ship-orders";
	}	
}
