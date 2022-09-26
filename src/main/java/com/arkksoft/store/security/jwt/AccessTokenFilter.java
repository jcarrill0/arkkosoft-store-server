package com.arkksoft.store.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.arkksoft.store.services.UsuarioDetailsService;

public class AccessTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UsuarioDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    public AccessTokenFilter() { }

    public AccessTokenFilter(JwtUtils jwtUtils, UsuarioDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = getToken(request);
        String username = jwtUtils.extractUsername(token);
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            SecurityContextHolder.getContext().setAuthentication(createAuthentication(token, userDetails, request));
        }

        chain.doFilter(request, response);
    }

    private Authentication createAuthentication(String token, UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = null;
        if (jwtUtils.validateToken(token, userDetails)) {
            authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }
        return authToken;
    }

    private String getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(auth -> auth.startsWith("Berear "))
                .map(auth -> auth.replace("Bearer ", ""))
                .orElseThrow(() -> new BadCredentialsException("INVALID_TOKEN"));
    }
}
