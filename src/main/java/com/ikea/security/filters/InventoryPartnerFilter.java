package com.ikea.security.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author nandk
 * ADDITIONAL SCOPE :
 * An authentication filter added to validate for any WRITE call. Only partners with valid credentials
 * are allowed to make POST/PUT/DELETE call. GET calls are permitted for all.
 * The X-Auth-Header is a Base64 encoded {clientId:clientSecret} value that is registered in the application.
 * Any calling application that wants to have WRITE access need to pass this header and will be assigned ROLE_ADMIN else will be responded with a forfidden (403) exception.
 * Disable this feature by setting inventory.partner.authentication.disabled as true during development profiles.
 */
@Component
public class InventoryPartnerFilter extends GenericFilter {

    private static final String X_AUTH_HEADER="X-Auth-Header";

    @Value("${inventory.partner.secret}")
    private String partnerSecret;

    @Value("${inventory.partner.clientId}")
    private String partnerClientId;

    //Disable spring security authentication checks.
    @Value("${inventory.partner.authentication.disbaled}")
    private Boolean isPartnerAuthenticationDisabled;


    private String partnerCredentials;

    @PostConstruct
    public void initialConfig(){
        partnerCredentials=new String (Base64.getEncoder().encode((partnerClientId+":"+partnerSecret).getBytes()), StandardCharsets.UTF_8);
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String xAuthHeader= ((HttpServletRequest)servletRequest).getHeader(X_AUTH_HEADER);
        // X-Auth-Header is available and matches to partner-credentials configured in the application else no authentication token added.
        if (isPartnerAuthenticationDisabled || (xAuthHeader!=null && xAuthHeader.equals(partnerCredentials))){
            //Assign a ROLE_ADMIN role for authenticated partners. In real scenarios this could be dynamically set to various roles like ROLE_USER,ROLE_ANONYMOUS
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("IkeaPartner",partnerCredentials,
                                                                                                         Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
