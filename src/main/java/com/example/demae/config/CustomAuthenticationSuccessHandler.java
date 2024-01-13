package com.example.demae.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        String targetUrl = determineTargetUrl(authentication);
//
//        if (response.isCommitted()) {
//            return;
//        }

		getRedirectStrategy().sendRedirect(request, response, "/api/users/main");
	}

//    private String determineTargetUrl(Authentication authentication) {
//        // 사용자 정의 로직에 따라 리다이렉션 대상 URL 결정
//        // 예: 권한에 따라 다른 페이지로 리다이렉트
//        // 이 예시에서는 기본적으로 홈 페이지로 리다이렉트
//        return "/main.html";
//    }
}
