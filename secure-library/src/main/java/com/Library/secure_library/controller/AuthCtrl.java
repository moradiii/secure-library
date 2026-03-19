package com.Library.secure_library.controller;

import com.Library.secure_library.model.Role;
import com.Library.secure_library.model.User;
import com.Library.secure_library.repository.UserRep;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthCtrl {

    private static final Logger logger = LoggerFactory.getLogger(AuthCtrl.class);

    private final UserRep userRep;
    private final PasswordEncoder pwdEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {

        if (userRep.findByUsername(user.getUsername()).isPresent()) {
            result.rejectValue("username", "error.user", "Username already exists");
        }

        if (result.hasErrors()) {
            return "register";
        }

        user.setPassword(pwdEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        userRep.save(user);
        logger.info("New user registered: {}", user.getUsername());

        return "redirect:/login";
    }
}