package org.utcn.socialapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocialAppApplication {

    private static final String FILE_LOCATION = "tmp/calendar.xlsx";

    public static void main(String[] args) {
        SpringApplication.run(SocialAppApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(UserRepository userRepository, ProfileRepository profileRepository, AuthService authService) {
//        return args -> {
//            User adminUser = new User("admin@admin.com", "$2a$12$fKoKk6zSvgelafEd5GM42OZ2pTp0" +
//                    "/VUj7Af2l5v5P8nwoTXeooCxK" /*password*/, Role.ADMIN);
//            adminUser.setEnabled(true);
//            Profile profile = new Profile(adminUser, "prenume1", "nume1",
//                    LocalDate.parse("1998-08-05"));
//            profileRepository.save(profile);
//
//            User adminUser2 = new User("admin2@admin.com", "$2a$12$fKoKk6zSvgelafEd5GM42OZ2pTp0" +
//                    "/VUj7Af2l5v5P8nwoTXeooCxK" /*password*/, Role.ADMIN);
//            adminUser.setEnabled(true);
//            Profile profile2 = new Profile(adminUser2, "prenume2", "nume2",
//                    LocalDate.parse("1998-08-05"));
//            profileRepository.save(profile2);
//        };
//    }
}
