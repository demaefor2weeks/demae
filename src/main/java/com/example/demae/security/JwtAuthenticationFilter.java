package com.example.demae.security;

import com.example.demae.dto.login.LoginRequestDto;
import com.example.demae.enums.UserRoleEnum;
import com.example.demae.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성 부분")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil ) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/logins");
    }

    @Override
    public Authentication attemptAuthentication (HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password,
                            null
                    )
            );
        } catch (Exception e) {
            log.error("인증 시도 중 오류 발생: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth){
        String username = ((UserDetailsImpl) auth.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl)auth.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username,role);
        jwtUtil.addJwtToCookie(token,response);
//
//        // 로그인 성공 시 main.html로 리다이렉트
//        try {
//            response.sendRedirect("templates/main.html");
//        } catch (IOException e) {
//            log.error("Redirect to main.html failed: " + e.getMessage());
//            // 에러 처리 로직 추가
//        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException fail)  {
        fail.getCause();
        response.setStatus(401);


    }
}
