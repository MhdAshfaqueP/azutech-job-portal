package com.example.jobportal.repository;

import com.example.jobportal.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long>{
    Optional<Admin> findByUsername(
            String username);
}
