package com.ercanbeyen.restaurantapplication.advice;

import com.ercanbeyen.restaurantapplication.exception.AlreadyExistsException;
import com.ercanbeyen.restaurantapplication.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(Exception exception, HttpServletRequest request) {
        return directToErrorPage(HttpStatus.NOT_FOUND, exception, request.getRequestURL().toString());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ModelAndView handleConflictExceptions(Exception exception, HttpServletRequest request) {
        return directToErrorPage(HttpStatus.CONFLICT, exception, request.getRequestURL().toString());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(Exception exception, HttpServletRequest request) {
        return directToErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, exception, request.getRequestURL().toString());
    }

    private ModelAndView directToErrorPage(HttpStatus httpStatus, Exception exception, String requestedUrl) {
        log.error("Request: {} raised {}", requestedUrl, exception.toString());

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("statusCode", httpStatus.value());
        modelAndView.addObject("message", exception.getMessage());
        modelAndView.addObject("url", requestedUrl);

        return modelAndView;
    }
}
