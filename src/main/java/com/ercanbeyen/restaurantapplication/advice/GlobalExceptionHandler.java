package com.ercanbeyen.restaurantapplication.advice;

import com.ercanbeyen.restaurantapplication.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(Exception exception, Model model) {
        return directToErrorPage(HttpStatus.NOT_FOUND, exception.getMessage(), model);
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalExceptions(Exception exception, Model model) {
        return directToErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), model);
    }

    private String directToErrorPage(HttpStatus httpStatus, String message, Model model) {
        model.addAttribute("statusCode", httpStatus.value());
        model.addAttribute("message", message);
        return "error";
    }
}
