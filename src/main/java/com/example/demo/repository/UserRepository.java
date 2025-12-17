package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Used for Login (Spring Security)
    Optional<User> findByEmail(String email);

    // Check if email exists before signup
    boolean existsByEmail(String email);
}