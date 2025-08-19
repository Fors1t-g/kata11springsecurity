package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ProfileController {
    @GetMapping("/user")
    public String userPage(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "user";
    }
}
