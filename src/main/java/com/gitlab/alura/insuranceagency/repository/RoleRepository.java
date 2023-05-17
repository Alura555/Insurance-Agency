package com.gitlab.alura.insuranceagency.repository;

import com.gitlab.alura.insuranceagency.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByTitle(String title);

}
