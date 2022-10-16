package com.example.jiraproject.security.config;

import com.example.jiraproject.security.filter.CustomAuthenticationFilter;
import com.example.jiraproject.security.filter.CustomOncePerRequestFilter;
import com.example.jiraproject.security.util.JwtUtil;
import com.example.jiraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(daoAuthenticationProvider(), userService, jwtUtil);
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        CustomOncePerRequestFilter customOncePerRequestFilter = new CustomOncePerRequestFilter(jwtUtil);
        //disable CORS will throw an exception
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().anyRequest().authenticated(); //permitAll() to test APIs, authenticated() to test Security
        http.formLogin().disable();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(customOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
