package com.media.media_project;

import com.media.media_project.Models.AuthUser;
import com.media.media_project.Repo.AuthUserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder {

    @Bean
    public CommandLineRunner seedData(AuthUserRepo authUserRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (authUserRepo.findByUsername("admin").isEmpty()) {
                AuthUser user = new AuthUser();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin123"));
                authUserRepo.save(user);
                System.out.println("Seeded User into the database.");
            }
        };
    }
}
