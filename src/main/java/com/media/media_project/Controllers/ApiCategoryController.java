package com.media.media_project.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.media.media_project.Models.Category;
import com.media.media_project.Repo.CategoryRepo;
import org.springframework.web.bind.annotation.GetMapping;

@RestController()
@RequestMapping("/api/category")
public class ApiCategoryController {
    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/list")
    public List<Category> list() {
        return categoryRepo.findAll();
    }

}
