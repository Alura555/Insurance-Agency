package com.gitlab.alura.insuranceagency.repository;

import com.gitlab.alura.insuranceagency.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndIsActive(String email, Boolean isActive);
    Optional<User> findByUsername(String username);
    Page<User> findAllByIsActive(Pageable pageable, Boolean isActive);

    Optional<User> findByIdAndIsActive(Long id, Boolean isActive);
}
