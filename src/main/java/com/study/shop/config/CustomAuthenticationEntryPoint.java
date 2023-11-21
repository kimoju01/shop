package com.study.shop.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // 인증되지 않은 사용자가 Ajax로 리소스를 요청할 경우 Unauthorized 에러 발생시킴
    // 나머지 경우는 로그인 페이지로 리다이렉트 시킴

    // 인증에 실패한 경우에 호출 됨. 어떻게 응답할지.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Ajax를 통한 요청인지 확인 (헤더 값을 보고 Ajax인지 식별)
        if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {    // 일반적인 요청인 경우 로그인 페이지로 리다이렉트
            response.sendRedirect("/members/login");
        }
    }

}
