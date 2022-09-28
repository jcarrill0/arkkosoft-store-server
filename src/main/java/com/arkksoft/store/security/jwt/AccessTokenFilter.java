package com.arkksoft.store.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.arkksoft.store.services.UsuarioDetails;
import com.arkksoft.store.services.UsuarioDetailsService;

@Component
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
       
        if(token != null) {
            String email =  jwtUtils.extractEmail(token);
            
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsuarioDetails userDetails = (UsuarioDetails) userDetailsService.loadUserByUsername(email);
                SecurityContextHolder.getContext().setAuthentication(createAuthentication(token, userDetails, request));
            } 
        } else {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private Authentication createAuthentication(String token, UsuarioDetails userDetails, HttpServletRequest request) {
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
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.replace("Bearer ", "");
        }

        return token;
    }
}
