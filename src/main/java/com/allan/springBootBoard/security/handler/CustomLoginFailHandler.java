package com.allan.springBootBoard.security.handler;

import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import com.allan.springBootBoard.web.error.code.ErrorCode;
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
        String errorPath = "";

        log.error("loginFailHandler call!!");

        if(exception instanceof UserNotFoundException){    // 입력 아이디가 존재하지 않을때 예외 발생.
            log.error("UsernameNotFountException!!");
            errorCode = ErrorCode.USER_ID_NOT_FOUND.getCode();
            errorMsg = ErrorCode.USER_ID_NOT_FOUND.getMessage();
        }
        if(exception instanceof BadCredentialsException){   // 아이디와 비밀빈호가 일치하지 않을 때 예외 발생.
            log.error("BadCredentialsException!!");
            errorCode = ErrorCode.INPUT_ID_NOT_MATCH.getCode();
            errorMsg = ErrorCode.INPUT_ID_NOT_MATCH.getMessage();
        }else{
            log.error("exception : " + exception);
        }

        errorPath = "/serviceLogin/loginForm?errorMessage=" + errorMsg;
        request.getRequestDispatcher(errorPath).forward(request,response);
    }
}
