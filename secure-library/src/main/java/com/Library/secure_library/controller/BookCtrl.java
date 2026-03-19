package com.Library.secure_library.controller;

import com.Library.secure_library.repository.BookRep;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BookCtrl {

    private final BookRep bookRep;

    @GetMapping("/books")
    public String listBooks(Model model, Authentication auth) {
        model.addAttribute("books", bookRep.findAll());
        model.addAttribute("username", auth.getName());
        return "books";
    }
}
