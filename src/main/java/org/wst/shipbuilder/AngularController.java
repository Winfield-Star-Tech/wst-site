package org.wst.shipbuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.wst.shipbuilder.data.EveUserRepository;

@Controller
public class AngularController extends WebMvcConfigurerAdapter {
	@Autowired
	private EveUserRepository userRepository;
	
	@Override
	 public void addViewControllers(ViewControllerRegistry registry) {
	  registry.addViewController("/angular").setViewName("angular");
	  registry.addViewController("/angular/partial/useradmin").setViewName("partials/useradmin::content");
	  registry.addViewController("/angular/partial/main").setViewName("partials/main::content");
	  registry.addViewController("/angular/partial/hephaestus").setViewName("partials/hephaestus::content");
	  registry.addViewController("/angular/partial/shiporders").setViewName("partials/shiporders::content");
	  registry.addViewController("/angular/partial/unauthorised").setViewName("partials/unauthorised::content");
	 }
	
	@Bean
	  public EmbeddedServletContainerCustomizer customizer() {
	    EmbeddedServletContainerCustomizer e = new EmbeddedServletContainerCustomizer() {
			
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
	    		container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/unauthorised"));
				
			}
		};
		return e;
	   
	  }
}
