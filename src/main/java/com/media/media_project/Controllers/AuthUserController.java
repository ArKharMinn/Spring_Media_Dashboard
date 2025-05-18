package com.media.media_project.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthUserController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home Page");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
