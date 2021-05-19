package com.allan.springBootBoard.web.user.controller;

import com.allan.springBootBoard.web.user.domain.model.LoginForm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    UserDetailsService userDetailsService;

    @NonNull
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/loginForm")
    public String loginForm(Model model, @CookieValue(value = "IDCOOKIE", required = false) Cookie idCookie,
                            @RequestParam(name = "errorMessage", required = false, defaultValue = "null") String errorMessage){
        LoginForm form = new LoginForm();
        if(idCookie != null){
            String cookieData = null;
            try {
                cookieData = URLDecoder.decode(idCookie.getValue(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error("login cookieData decode process cause error!!: " + e.getMessage());
                e.printStackTrace();
            }

            // cookie needs null check.??
            form.setUserId(cookieData);
            form.setUseCookie(true);
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("loginForm", form);
        return "user/loginForm";
    }

}
