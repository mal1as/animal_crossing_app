package com.itmo.coursework.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomSecurityFilter extends GenericFilterBean {

    private static final List<String> SCIENTIST_URLS = Collections.singletonList("/backend/api/v1/experiment");
    private static final List<String> MANAGER_URLS = Arrays.asList("/backend/api/v1/reserve", "/backend/api/v1/report");
    private static final List<String> RESERVE_WORKER_URLS = Collections.emptyList();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.contains("/login") || requestURI.contains("/register") || authentication == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        // filter urls by role
        if (SCIENTIST_URLS.stream().anyMatch(requestURI::contains) && !roles.contains("ADMIN") && !roles.contains("SCIENTIST") ||
                MANAGER_URLS.stream().anyMatch(requestURI::contains) && !roles.contains("ADMIN") && !roles.contains("MANAGER") ||
                RESERVE_WORKER_URLS.stream().anyMatch(requestURI::contains) && !roles.contains("ADMIN") && !roles.contains("RESERVE_WORKER")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
