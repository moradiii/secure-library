package com.Library.secure_library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestCtrl {

    @GetMapping("/test-error")
    public String testError() {
        throw new RuntimeException("Test generic error page");
    }
}