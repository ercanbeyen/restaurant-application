package com.ercanbeyen.restaurantapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorPageController {
    @GetMapping("/error-details")
    public ModelAndView showErrorPage() {
        return new ModelAndView("error");
    }
}
