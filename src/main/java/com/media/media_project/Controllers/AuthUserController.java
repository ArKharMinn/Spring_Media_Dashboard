package com.media.media_project.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.media.media_project.Repo.CategoryRepo;
import com.media.media_project.Repo.PostRepo;

@Controller
public class AuthUserController {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private PostRepo postRepo;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        model.addAttribute("category", categoryRepo.count());
        model.addAttribute("post", postRepo.count());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
