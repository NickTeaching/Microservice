package com.workwaves.app.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	private final Environment env;
	
	@Autowired
	public WebSecurity(Environment env) {
		this.env = env;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
//		super.configure(http);
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests()
			.antMatchers(env.getProperty("api.h2console.url.path")).permitAll()
			.antMatchers(HttpMethod.POST,env.getProperty("api.registration.url.path")).permitAll()
			.antMatchers(HttpMethod.POST,env.getProperty("api.login.url.path")).permitAll()
			// all other request are validated by spring security
			.anyRequest().authenticated()
			.and()
			.addFilter(new AuthorizationFilter(authenticationManager(), env));
		// making api stateless
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); 
	}

	
}
