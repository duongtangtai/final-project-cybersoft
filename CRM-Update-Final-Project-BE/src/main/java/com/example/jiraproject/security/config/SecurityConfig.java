package com.example.jiraproject.security.config;

import com.example.jiraproject.security.filter.CustomOncePerRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
    private final CustomOncePerRequestFilter customOncePerRequestFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CROSS-ORIGIN RESOURCE SHARING -> ENABLE
        //CROSS-SITE REQUEST FORGERY -> DISABLE -> RECEIVE INPUT FROM BROWSERS
        http.cors().configurationSource(corsConfigurationSource())
                .and().csrf().disable();

        //STATELESS -> WON'T CREATE ANY SESSIONS
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //contextPath = "/jira/api" (config in application.yml)
        //api path = "/v1"
        //swagger path = /jira/api/swagger-ui.html
        http.antMatcher("/v1/**").authorizeRequests()
                .antMatchers("/v1/auth/login/**").permitAll()
                .antMatchers("/v1/auth/refresh-token/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/files/**").permitAll()
                .anyRequest().authenticated();

        http.formLogin().disable();
        http.addFilterBefore(customOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
