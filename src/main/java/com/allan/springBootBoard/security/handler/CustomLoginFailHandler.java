package com.allan.springBootBoard.security.handler;

import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomLoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorCode = "";  // 에러 코드
        String errorMsg = "";   // 에러 메세지

        if(exception instanceof UsernameNotFoundException){    // 인증 요구가 거부됐을 때 던지는 예외
            // 알맞는 에러코드 및 에러 메시지 설정.
        }
        if(exception instanceof BadCredentialsException){
            // 알맞는 에러코드 및 에러 메시지 설정.
        }
        log.info("CustomLoginFailHandler exception.getMessage(): " + exception.getMessage());
        response.sendRedirect(request.getContextPath() + "/user/loginForm?errorMessage=" + exception.getMessage());
    }
}
