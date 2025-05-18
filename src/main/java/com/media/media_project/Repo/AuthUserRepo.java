package com.media.media_project.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.media.media_project.Models.AuthUser;
import java.util.List;

public interface AuthUserRepo extends JpaRepository<AuthUser, Long> {
    List<AuthUser> findByUsername(String username);
}
