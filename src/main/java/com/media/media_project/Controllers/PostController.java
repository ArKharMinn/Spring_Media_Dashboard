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

import com.media.media_project.Models.Category;
import com.media.media_project.Models.Post;
import com.media.media_project.Repo.CategoryRepo;
import com.media.media_project.Repo.PostRepo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
        model.addAttribute("post", post);
        return "redirect:/post/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute Post post, @PathVariable Long id) {
        Post existingPost = postRepo.findById(id).orElse(null);
        if (existingPost != null) {
            existingPost.setName(post.getName());
            existingPost.setCategory_id(post.getCategory_id());
            existingPost.setDescription(post.getDescription());
        }
        return "redirect:/post/list";
    }

    @PostMapping("/upload")
    public String uploadPost(@ModelAttribute Post post, @RequestParam("imageFile") MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                String originalFileName = image.getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalFileName;

                String uploadDir = "uploads/";

                Files.createDirectories(Paths.get(uploadDir));

                Path filePath = Paths.get(uploadDir + fileName);

                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                post.setImage_url(fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/post/create?error";
            }
        }

        postRepo.save(post);

        return "redirect:/post/list";
    }

}
