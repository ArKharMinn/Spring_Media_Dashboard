package com.media.media_project.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.media.media_project.Models.Category;
import com.media.media_project.Repo.CategoryRepo;
import com.media.media_project.Repo.PostRepo;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private PostRepo postRepo;

    @GetMapping("/list")
    public String list(Model model) {

        List<Category> category = categoryRepo.findAll();
        model.addAttribute("category", category);
        model.addAttribute("title", "Category List");
        return "category/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (postRepo.countByCategory_Id(id) > 0) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete category: it is still in use");
            return "redirect:/category/list";
        }
        categoryRepo.deleteById(id);
        return "redirect:/category/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("title", "Create Category");
        return "category/create";
    }

    @PostMapping("/create/form")
    public String form(@ModelAttribute @Valid Category category, BindingResult bindingResult) {

        categoryRepo.save(category);
        return "redirect:/category/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        model.addAttribute("title", "Edit Category");
        Category category = categoryRepo.findById(id).orElse(null);
        model.addAttribute("category", category);
        return "category/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute Category category, @PathVariable Long id) {
        Category existingCategory = categoryRepo.findById(id).orElse(null);

        if (existingCategory != null) {
            existingCategory.setName(category.getName());
            categoryRepo.save(existingCategory);
        }
        return "redirect:/category/list";
    }

}
