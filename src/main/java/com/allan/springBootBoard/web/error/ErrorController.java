package com.allan.springBootBoard.web.error;

import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/userNotFound")
    public String userNotFound(Model model, @RequestParam("message") String message){
        model.addAttribute("message", message);
        return "error/userNotFound";
    }
}
