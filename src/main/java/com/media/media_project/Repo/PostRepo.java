package com.media.media_project.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.media.media_project.Models.Post;

public interface PostRepo extends JpaRepository<Post, Long> {

}
