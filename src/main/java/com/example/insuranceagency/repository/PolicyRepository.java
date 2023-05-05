package com.example.insuranceagency.repository;

import com.example.insuranceagency.entity.Policy;
import com.example.insuranceagency.entity.User;
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

    Optional<Policy> findByIdAndManagerEmail(Long id, String email);

    List<Policy> findAllByManager(User manager);

    List<Policy> findAllByManagerEmail(String managerEmail);

    List<Policy> findAllByClientEmail(String clientEmail);


}
