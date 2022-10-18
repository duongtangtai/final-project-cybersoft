package com.example.jiraproject.security.config;

import com.example.jiraproject.security.filter.CustomOncePerRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOncePerRequestFilter customOncePerRequestFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CROSS-ORIGIN RESOURCE SHARING -> ENABLE
        //CROSS-SITE REQUEST FORGERY -> DISABLE -> RECEIVE INPUT FROM BROWSERS
        http.cors().and().csrf().disable();

        //STATELESS -> WON'T CREATE ANY SESSION
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //contextPath = "/jira/api"
        //api path = "/v1"
        //swagger path = /jira/api/swagger-ui.html
        http.antMatcher("/v1/**").authorizeRequests()
                .antMatchers("/v1/auth/login/**").permitAll()
                .anyRequest().authenticated();

        http.formLogin().disable();
        http.addFilterBefore(customOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
