package com.ezgroceries.shoppinglist.security;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class JWTTokenFilter implements Filter {

    private final String tokenHeaderName;
    private final SecurityManager securityTokenManager;

    public JWTTokenFilter(String tokenHeaderName, SecurityManager securityTokenManager) {
        this.tokenHeaderName = tokenHeaderName;
        this.securityTokenManager = securityTokenManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String tokenHeaderValue = request.getHeader(tokenHeaderName);
        Authentication authenticatedResult = new PreAuthenticatedAuthenticationToken(null, null, null);
        authenticatedResult.setAuthenticated(false);
        if (!StringUtils.isEmpty(tokenHeaderValue)) {
            try {
                Claims claims = securityTokenManager.validateJWT(tokenHeaderValue);
                UUID userId = UUID.fromString(claims.getSubject());
                ShopUserPrincipal shopUserPrincipal = new ShopUserPrincipal(userId);
                authenticatedResult = new PreAuthenticatedAuthenticationToken(shopUserPrincipal, null, null);
                authenticatedResult.setAuthenticated(true);
            } catch (Exception e) {
                log.error("Error in processing tokenHeaderValue, user will not be authenticated", e);
            }
        }
        SecurityContextHolder.getContext().setAuthentication(authenticatedResult);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}