package com.example.jobportal.service;

import com.example.jobportal.entity.Admin;
import com.example.jobportal.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository,
                        PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin login(String username, String rawPassword) {
        return adminRepository.findByUsername(username)
                .filter(admin ->
                        passwordEncoder.matches(rawPassword, admin.getPassword()))
                .orElse(null);
    }
}
