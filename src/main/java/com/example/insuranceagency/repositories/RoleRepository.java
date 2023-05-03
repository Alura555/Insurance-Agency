package com.example.insuranceagency.repositories;

import com.example.insuranceagency.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
