package com.allan.springBootBoard.web.user.controller;

import com.allan.springBootBoard.web.user.domain.model.LoginForm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
@RequiredArgsConstructor
@RequestMapping("/serviceLogin")
public class LoginController {

    @NonNull
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/loginForm")
    public String loginForm(Model model, @CookieValue(value = "IDCOOKIE", required = false) Cookie idCookie){
        LoginForm form = new LoginForm();
        setCookieVal(idCookie, form);
        model.addAttribute("loginForm", form);
        return "user/loginForm";
    }

    /*
    * loginForm 에서 post 로그인 요청 후,
    * CustomLoginFailHandler에서 넘어온 예외 메시지로 로그인 창에 전달하기 위한
    * post 메소드
    * (단순 view 파일에 model 정보만 추가하는 형태이기에 PGR 패턴을 적용하지 않음.
    * errorMessage 노출 하는것이 보안상, 더 안좋다고 생각함.)
    * */
    @PostMapping("/loginForm")
    public String loginFormByLonginFailHandler(Model model, @CookieValue(value = "IDCOOKIE", required = false) Cookie idCookie,
                                               @RequestParam(name = "errorMessage") String errorMessage){
        LoginForm form = new LoginForm();
        setCookieVal(idCookie, form);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("loginForm", form);
        return "user/loginForm";
    }

    private void setCookieVal(Cookie idCookie, LoginForm form) {
        if(idCookie != null){
            String cookieData = idCookie.getValue();
            form.setUserId(cookieData);
            form.setUseCookie(true);
        }
    }

}
