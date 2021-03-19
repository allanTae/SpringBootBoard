package com.allan.springBootBoard.web.error;

import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/error")
@Slf4j
public class CustomErrorController implements ErrorController {

    private String viewPath = "error/";

    @RequestMapping("/serverError")
    public String defaultHandleError(HttpServletRequest request, HttpServletResponse response) {
        log.info("customErrorController's defaultHandleError() call!!");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = Integer.valueOf(status.toString());
        if(statusCode == HttpStatus.NOT_FOUND.value()){
            log.info("viewName: " + viewPath + "404");
            return viewPath + "404";
        }
        return "defaultHandleError";
    }

    @Override
    public String getErrorPath() {
        return null;
    }

}
