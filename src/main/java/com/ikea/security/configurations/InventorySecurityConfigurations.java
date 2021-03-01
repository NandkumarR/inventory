package com.ikea.security.configurations;

import com.ikea.security.filters.InventoryPartnerFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.annotation.Resource;

/**
 * @author nandk
 * ADDITIONAL SCOPE :
 * Enables web security for the application where it validates different requests based on the
 * rules defined below.
 * Any POST/PUT/DELETE call needs to be authenticated, GET calls are open to all.
 */
@EnableWebSecurity
@Configuration
public class InventorySecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Resource
    private InventoryPartnerFilter inventoryPartnerFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().cacheControl();
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/inventory/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/inventory/**").authenticated()
                .antMatchers(HttpMethod.DELETE,"/inventory/**").authenticated()
                .antMatchers(HttpMethod.GET,"/inventory/**").permitAll()
                .and().addFilterAfter(inventoryPartnerFilter, AnonymousAuthenticationFilter.class);
    }
}
