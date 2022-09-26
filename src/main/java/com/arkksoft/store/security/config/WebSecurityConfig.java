package com.arkksoft.store.security.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.arkksoft.store.security.jwt.*;
import com.arkksoft.store.services.UsuarioDetailsService;

/*
 * The prePostEnabled property enables Spring Security pre/post annotations.
 * The securedEnabled property determines if the @Secured annotation should be enabled.
 * The jsr250Enabled property allows us to use the @RoleAllowed annotation.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    @Autowired
    UserDetailsService usuarioDetailsService;

    @Autowired
    private AccessTokenEntryPoint accessTokenEntryPoint;

    @Value("${cross.origin.url}")
    private String urlCors;


    @Bean
    public AccessTokenFilter authenticationJwtTokenFilter() {
        return new AccessTokenFilter();
    }

    @Bean
    public AccessTokenFilter authenticationJwtTokenFilter(JwtUtils jwtUtils, UsuarioDetailsService userDetailsService) {
        return new AccessTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(accessTokenEntryPoint).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()        
        .antMatchers("/api/v1/auth/signin").permitAll()     
        .antMatchers("/api/v1/auth/signup").permitAll()
        .anyRequest().authenticated();

    http
        .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(false)
          .ignoring()
          .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins(urlCors.split(","))
                    .allowedMethods("*")
                    .allowedHeaders("*");
            }
        };
    }
}
