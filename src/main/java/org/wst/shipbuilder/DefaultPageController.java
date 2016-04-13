package org.wst.shipbuilder;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import org.wst.shipbuilder.data.EveUser;
import org.wst.shipbuilder.data.EveUserRepository;

@Controller

@Configuration
@EnableOAuth2Client

public class DefaultPageController extends WebSecurityConfigurerAdapter {
	private int donations = 0;
	
	@Autowired
	private EveUserRepository userRepository;
	
	@RequestMapping("/")
	public String greeting(Model model) {
		return "index";
	}
	@RequestMapping("/admin")
	public String admin(Model model) {
		Iterable<EveUser> users = userRepository.findAll();
		model.addAttribute("users", users);
		return "admin";
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

	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off	
		http.antMatcher("/**")
			.authorizeRequests()				
				.antMatchers("/", "/login**", "/webjars/**", "/home", "/resources/**", "/toshuu").permitAll()
				.antMatchers("/admin").hasRole("ADMIN")
				.anyRequest().authenticated()
			.and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
			.and().logout().logoutSuccessUrl("/").permitAll()
			.and().csrf().csrfTokenRepository(csrfTokenRepository())
			.and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
			.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
		// @formatter:on
	}

	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(
			OAuth2ClientContextFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(-101);
		return registration;
	}

	@Bean
	public Filter ssoFilter() {
		OAuth2ClientAuthenticationProcessingFilter evessoFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/evesso");
		OAuth2RestTemplate evessoTemplate = new OAuth2RestTemplate(evesso(), oauth2ClientContext) ;
		evessoFilter.setRestTemplate(evessoTemplate);
		EveUserInfoTokenServices tokenServices =  new EveUserInfoTokenServices(evessoResource().getUserInfoUri(), evesso().getClientId());
		tokenServices.setEveUserRepository(userRepository);
		evessoFilter.setTokenServices(tokenServices);
		return evessoFilter;
	}

	@Bean
	@ConfigurationProperties("evesso.client")
	OAuth2ProtectedResourceDetails evesso() {
		return new AuthorizationCodeResourceDetails();
	}
	
	

	@Bean
	@ConfigurationProperties("evesso.resource")
	ResourceServerProperties evessoResource() {
		return new ResourceServerProperties();
	}

	private Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					if (cookie == null || token != null && !token.equals(cookie.getValue())) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
	@Bean public RequestContextListener requestContextListener(){
	    return new RequestContextListener();
	} 
}
