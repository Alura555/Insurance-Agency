package com.gitlab.alura.insuranceagency.repository;

import com.gitlab.alura.insuranceagency.entity.InsuranceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface InsuranceTypeRepository extends JpaRepository<InsuranceType, Long> {
    @Override
    List<InsuranceType> findAll();

    List<InsuranceType> findAllByIsActive(Boolean isActive);

    Page<InsuranceType> findAllByIsActive(Pageable pageable, Boolean isActive);

    Optional<InsuranceType> findByIdAndIsActive(Long id, boolean b);
}
