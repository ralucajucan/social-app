package org.utcn.socialapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.utcn.socialapp.auth.AuthService;
import org.utcn.socialapp.auth.registration.RegisterDTO;
import org.utcn.socialapp.profile.ProfileRepository;
import org.utcn.socialapp.user.Role;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserRepository;

@SpringBootApplication
public class SocialAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialAppApplication.class, args);
    }

    CommandLineRunner run(UserRepository userRepository, ProfileRepository profileRepository, AuthService authService){
        return args -> {
            User adminUser = new User("admin@admin.com", "$2a$12$fKoKk6zSvgelafEd5GM42OZ2pTp0" +
                    "/VUj7Af2l5v5P8nwoTXeooCxK" /*password*/, Role.ADMIN);
            adminUser.setEnabled(true);
            userRepository.save(adminUser);


            authService.register(new RegisterDTO(
                    "user@user.com",
                    "$2a$12$fKoKk6zSvgelafEd5GM42OZ2pTp0/VUj7Af2l5v5P8nwoTXeooCxK" /*password*/,
                    "Raluca",
                    "Jucan",
                    "1998-08-05"));
        };
    }
}
