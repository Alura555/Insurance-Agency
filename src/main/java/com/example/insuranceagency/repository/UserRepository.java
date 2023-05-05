package com.example.insuranceagency.repository;

import com.example.insuranceagency.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT c FROM User c WHERE c.email = ?1")
    User findByEmail(String email);
}
