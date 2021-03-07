package com.allan.springBootBoard.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 아이디 체크를 위한 쿠키.
        Cookie idCookie = new Cookie("IDCOOKIE", request.getParameter("userId"));
        idCookie.setPath("/");
        if(Boolean.valueOf(request.getParameter("useCookie"))){
            idCookie.setMaxAge(60*60*24*7);
            log.info("로그인 쿠키를 생성합니다.");
        }else{
            idCookie.setMaxAge(0);
        }
        response.addCookie(idCookie);
        response.sendRedirect(request.getContextPath() + "/board/getBoardList");
    }
}
