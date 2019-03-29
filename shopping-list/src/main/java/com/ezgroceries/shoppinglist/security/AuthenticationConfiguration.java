package com.ezgroceries.shoppinglist.security;

import com.ezgroceries.shoppinglist.exception.UnauthorizedException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@EnableWebSecurity
public class AuthenticationConfiguration extends WebSecurityConfigurerAdapter {

    private final String tokenHeaderName;
    private final SecurityManager securityManager;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public AuthenticationConfiguration(@Value("${security.token}") String tokenHeaderName, SecurityManager securityManager, HandlerExceptionResolver handlerExceptionResolver) {
        this.tokenHeaderName = tokenHeaderName;
        this.securityManager = securityManager;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring()
                .mvcMatchers(HttpMethod.POST, "/registrations")
                .mvcMatchers(HttpMethod.POST, "/verifications")
                .mvcMatchers(HttpMethod.POST, "/logins")
                .mvcMatchers(HttpMethod.GET, "/cocktails/**")
                .mvcMatchers(HttpMethod.GET, "/email-stubs")
                .mvcMatchers(HttpMethod.GET, "/actuator/**")
                .mvcMatchers(HttpMethod.GET, "/swagger-ui.html")
                .mvcMatchers(HttpMethod.GET, "/swagger-resources/**")
                .mvcMatchers(HttpMethod.GET, "/webjars/springfox-swagger-ui/**")
                .mvcMatchers(HttpMethod.GET, "/v2/api-docs");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/users/{userId}/**").access("principal.isAuthorisedToRequest(#userId)")
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JWTTokenFilter(tokenHeaderName, securityManager), BasicAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());
    }

    @SneakyThrows
    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> handlerExceptionResolver.resolveException(request, response, null, new UnauthorizedException(
                "Authorised personnel only"));
    }

    @SneakyThrows
    private AccessDeniedHandler accessDeniedHandler() {
        return (httpServletRequest, httpServletResponse, exception) -> handlerExceptionResolver.resolveException(httpServletRequest,
                httpServletResponse, null, new UnauthorizedException("Welcome to the Forbidden Forest..."));
    }

}
