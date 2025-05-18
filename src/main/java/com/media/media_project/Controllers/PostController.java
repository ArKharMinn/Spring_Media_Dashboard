package com.media.media_project.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.media.media_project.Models.Category;
import com.media.media_project.Models.Post;
import com.media.media_project.Repo.CategoryRepo;
import com.media.media_project.Repo.PostRepo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostRepo postRepo;
    private CategoryRepo categoryRepo;

    @GetMapping("/list")
    public String list(Model model) {
        List<Post> post = postRepo.findAll();
        model.addAttribute("post", post);
        model.addAttribute("title", "Post List");
        return "post/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        List<Category> category = categoryRepo.findAll();
        model.addAttribute("category", category);
        model.addAttribute("title", "Create posts");
        return "post/create";
    }

    @PostMapping("/upload")
    public String postMethodName(@ModelAttribute Post post) {
        postRepo.save(post);
        return "redirect:/post/list";
    }

}
