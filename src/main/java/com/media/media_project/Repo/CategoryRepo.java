package com.media.media_project.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.media.media_project.Models.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {

}
