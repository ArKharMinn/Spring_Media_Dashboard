package com.media.media_project.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.media.media_project.Models.Category;
import com.media.media_project.Models.Post;
import com.media.media_project.Repo.CategoryRepo;
import com.media.media_project.Repo.PostRepo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostRepo postRepo;

    @Autowired
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

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        postRepo.deleteById(id);
        return "redirect:/post/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Post post = postRepo.findById(id).orElse(null);
        List<Category> category = categoryRepo.findAll();
        model.addAttribute("category", category);
        model.addAttribute("title", "Edit posts");
        model.addAttribute("post", post);
        return "post/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category.id") Long categoryId,
            @RequestParam(value = "image_url", required = false) MultipartFile image,
            RedirectAttributes redirectAttributes) {
        try {
            Post existingPost = postRepo.findById(id).orElse(null);
            if (existingPost == null) {
                redirectAttributes.addFlashAttribute("error", "Post not found.");
                return "redirect:/post/list";
            }

            existingPost.setName(name);
            existingPost.setDescription(description);

            if (categoryId != null) {
                Category category = categoryRepo.findById(categoryId).orElse(null);
                if (category != null) {
                    existingPost.setCategory(category);
                }
            }

            if (image != null && !image.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                String uploadDir = "src/main/resources/static/uploads/";
                Files.createDirectories(Paths.get(uploadDir));
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                existingPost.setImage_url(fileName);
            }

            postRepo.save(existingPost);
            redirectAttributes.addFlashAttribute("success", "Post updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error updating post: " + e.getMessage());
            return "redirect:/post/edit/" + id;
        }

        return "redirect:/post/list";
    }

    @PostMapping("/upload")
    public String uploadPost(@ModelAttribute Post post,
            @RequestParam("imageFile") MultipartFile image) {
        try {
            if (post.getCategory() == null || post.getCategory().getId() == null) {
                throw new IllegalArgumentException("Category is missing");
            }

            Category category = categoryRepo.findById(post.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

            post.setCategory(category);

            if (!image.isEmpty()) {
                String originalFileName = image.getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalFileName;

                String uploadDir = "src/main/resources/static/uploads/";
                Files.createDirectories(Paths.get(uploadDir));
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                post.setImage_url(fileName);
            } else {
                post.setImage_url("default");
            }

            postRepo.save(post);
            return "redirect:/post/list";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/post/create?error";
        }
    }

}
