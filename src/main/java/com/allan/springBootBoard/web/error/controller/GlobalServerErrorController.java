package com.allan.springBootBoard.web.error.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/error")
@Slf4j
public class GlobalServerErrorController implements ErrorController {

    private String viewPath = "error/";

    @RequestMapping("/serverError")
    public String defaultHandleError(HttpServletRequest request, HttpServletResponse response) {
        log.error("GlobalServerErrorController's defaultHandleError() call!!");
        Object status = response.getStatus();
        Integer statusCode = Integer.valueOf(status.toString());
        if(statusCode == HttpStatus.NOT_FOUND.value()){
            return viewPath + "404";
        }
        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
            return viewPath + "500";
        }
        return "defaultHandleError";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
