package org.wst.shipbuilder.security;

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
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import org.wst.shipbuilder.data.EveUserRepository;
@Configuration
@EnableOAuth2Client
public class WSTSecurityConfigurerAdapter  extends WebSecurityConfigurerAdapter  {

	
	@Autowired
	private EveUserRepository userRepository;
	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off	
		http.antMatcher("/**")
			.authorizeRequests()				
				.antMatchers("/", 
						"/login**", "/webjars/**",  
						"/resources/**", "/toshuu",
						"/rest/users/**",
						"/angular/", "/angular/partial/main", "/angular/partial/about", "/mykey").permitAll()
				.antMatchers("/admin").hasRole("ADMIN")
				.anyRequest().hasRole("WST_USER")
//				.anyRequest().authenticated()
			.and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("partials/loginerror::content"))
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
