package com.example.demae.config;

import com.example.demae.jwt.JwtUtil;
import com.example.demae.security.JwtAuthenticationFilter;
import com.example.demae.security.JwtAuthorizationFilter;
import com.example.demae.security.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
@Slf4j
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter()throws Exception{
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter(jwtUtil,userDetailsService);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((sessionManagement)->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests)->
                        authorizeHttpRequests
                                .requestMatchers("/api/users/**").permitAll()
                                .requestMatchers("/api/products").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.GET,"/instructors").hasAnyRole("USER","ADMIN")
//                                .requestMatchers(HttpMethod.POST,"/instructors").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.GET,"/api/lectures/**").hasAnyRole("USER","ADMIN")
//                                .requestMatchers(HttpMethod.POST,"/api/lectures").hasRole("ADMIN")
//                                .requestMatchers("/api/comments/**").hasAnyRole("USER","ADMIN")
                                .anyRequest().authenticated()

        );

        http.formLogin((formLogin) ->
                formLogin
                        .loginPage("/api/users/main")
                        .loginProcessingUrl("/api/users/asdf")
        );

        // UsernamePasswordAuthenticationFilter보다 먼저 실행
        http.addFilterBefore(jwtAuthorizationFilter(),JwtAuthenticationFilter.class); // 인가 전 인증
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // 인가전 UserName, Password확인

        return http.build();
    }



}

