package com.example.insuranceagency.repository;

import com.example.insuranceagency.entity.Policy;
import com.example.insuranceagency.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    @Override
    List<Policy> findAll();

    @Override
    Optional<Policy> findById(Long id);

    Optional<Policy> findByIdAndManager(Long id, User user);

    Optional<Policy> findByIdAndClientEmail(Long id, String email);

    Page<Policy> findAll(Specification<Policy> spec, Pageable pageable);
}
