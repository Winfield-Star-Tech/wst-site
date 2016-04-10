package org.wst.shipbuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	@RequestMapping("/login")
	public String login(Model model) {
		return "login";
	}	
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}
}
