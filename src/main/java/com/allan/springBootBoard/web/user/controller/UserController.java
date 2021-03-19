package com.allan.springBootBoard.web.user.controller;

import com.allan.springBootBoard.web.user.domain.model.LoginForm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    UserDetailsService userDetailsService;

    @NonNull
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/loginForm")
    public String loginForm(Model model, @CookieValue(value = "IDCOOKIE", required = false) Cookie idCookie,
                            @RequestParam(name = "errorMessage", required = false, defaultValue = "null") String errorMessage){
        LoginForm form = new LoginForm();
        if(idCookie != null){
            form.setUserId(idCookie.getValue());
            form.setUseCookie(true);
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("loginForm", form);
        return "user/loginForm";
    }

}
