package org.utcn.socialapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.utcn.socialapp.profile.Profile;
import org.utcn.socialapp.profile.ProfileRepository;
import org.utcn.socialapp.user.Role;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserRepository;

import java.time.LocalDate;

@SpringBootApplication
public class SocialAppApplication {

    //    public static void main(String[] args) {
//        SpringApplication.run(SocialAppApplication.class, args);
//    }
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(SocialAppApplication.class, args);
        UserRepository userRepository = configurableApplicationContext.getBean(UserRepository.class);
        User adminUser = new User("admin@admin.com", "admin", "$2a$12$fKoKk6zSvgelafEd5GM42OZ2pTp0" +
                "/VUj7Af2l5v5P8nwoTXeooCxK" /*password*/, Role.ADMIN);
        User basicUser = new User("user@user.com", "user", "$2a$12$fKoKk6zSvgelafEd5GM42OZ2pTp0" +
                "/VUj7Af2l5v5P8nwoTXeooCxK" /*password*/, Role.USER);

        adminUser.setEnabled(true);
        ProfileRepository profileRepository = configurableApplicationContext.getBean(ProfileRepository.class);
        Profile adminProfile = new Profile(adminUser, "Raluca", "Jucan", LocalDate.parse("1998-08-05"));
        profileRepository.save(adminProfile);
        userRepository.save(basicUser);
    }
}
