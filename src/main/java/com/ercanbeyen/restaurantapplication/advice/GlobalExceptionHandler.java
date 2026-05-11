package com.ercanbeyen.restaurantapplication.advice;

import com.ercanbeyen.restaurantapplication.exception.AlreadyExistsException;
import com.ercanbeyen.restaurantapplication.exception.BadRequestException;
import com.ercanbeyen.restaurantapplication.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public RedirectView handleBadRequestException(Exception exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        return redirectToErrorPage(HttpStatus.BAD_REQUEST, exception, request.getRequestURL().toString(), redirectAttributes);
    }

    @ExceptionHandler(SecurityException.class)
    public RedirectView handleSecurityException(Exception exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        return redirectToErrorPage(HttpStatus.FORBIDDEN, exception, request.getRequestURL().toString(), redirectAttributes);
    }

    @ExceptionHandler(NotFoundException.class)
    public RedirectView handleNotFoundException(Exception exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        return redirectToErrorPage(HttpStatus.NOT_FOUND, exception, request.getRequestURL().toString(), redirectAttributes);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public RedirectView handleConflictException(Exception exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        return redirectToErrorPage(HttpStatus.CONFLICT, exception, request.getRequestURL().toString(), redirectAttributes);
    }

    @ExceptionHandler(Exception.class)
    public RedirectView handleGeneralExceptions(Exception exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        return redirectToErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, exception, request.getRequestURL().toString(), redirectAttributes);
    }

    private RedirectView redirectToErrorPage(HttpStatus status, Exception exception, String requestUrl, RedirectAttributes redirectAttributes) {
        log.error("Request: {} raised {}", requestUrl, exception.toString());

        redirectAttributes.addFlashAttribute("statusCode", status.value());
        redirectAttributes.addFlashAttribute("requestUrl", requestUrl);
        redirectAttributes.addFlashAttribute("message", exception.getMessage());

        return new RedirectView("/error-details");
    }
}
