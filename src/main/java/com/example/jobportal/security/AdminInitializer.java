package com.example.jobportal.security;

import com.example.jobportal.entity.Admin;
import com.example.jobportal.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdmins(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder,
            @Value("${admin.bootstrap.enabled:false}") boolean enabled
    ) {
        return args -> {
            if(enabled) {
                createAdminIfNotExists(
                        "admin1", "ad_1",
                        adminRepository, passwordEncoder
                );

                createAdminIfNotExists(
                        "admin2", "ad_2",
                        adminRepository, passwordEncoder
                );

                createAdminIfNotExists(
                        "admin3", "ad_3",
                        adminRepository, passwordEncoder
                );
            }
        };
    }

    private void createAdminIfNotExists(
            String username,
            String rawPassword,
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder
    ) {
        if (adminRepository.findByUsername(username).isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(rawPassword));
            adminRepository.save(admin);
            System.out.println("Created admin: " + username);
        }
    }
}
