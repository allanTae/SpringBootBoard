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
        log.info("customErrorController's defaultHandleError() call!!");
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object status = response.getStatus();
        Integer statusCode = Integer.valueOf(status.toString());
        log.info("status: " + status);
        log.info("statusCode: " + statusCode);
        if(statusCode == HttpStatus.NOT_FOUND.value()){
            log.info("viewName: " + viewPath + "404");
            return viewPath + "404";
        }
        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
            log.info("viewName: " + viewPath + "500");
            return viewPath + "500";
        }
        return "defaultHandleError";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
