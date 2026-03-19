package com.Library.secure_library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeCtrl {

    @GetMapping("/")
    public String home() {
        return "redirect:/books";
    }
}