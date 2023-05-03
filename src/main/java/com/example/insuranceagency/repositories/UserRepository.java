package com.example.insuranceagency.repositories;

import com.example.insuranceagency.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT c FROM User c WHERE c.email = ?1")
    public User findByEmail(String email);
}
