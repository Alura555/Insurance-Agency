package com.example.insuranceagency.repository;


import com.example.insuranceagency.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {
    @Override
    Optional<Offer> findById(Long aLong);

    @Override
    Page<Offer> findAll(Pageable pageable);

    @Override
    Page<Offer> findAll(Specification<Offer> spec, Pageable pageable);

    Offer findTopByOrderByPriceDesc();

    Offer findTopByOrderByPrice();

}
