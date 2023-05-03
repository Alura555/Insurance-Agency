package com.example.insuranceagency.services;

import com.example.insuranceagency.entities.Offer;
import com.example.insuranceagency.filters.OfferFilter;
import com.example.insuranceagency.services.implementation.OfferServiceImpl;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OfferService {
    Page<Offer> findAll(int page, int size, OfferFilter offerFilter, int sort);

    Offer findById(Long id);

    Integer getMaxPrice();

    Integer getMinPrice();

    List<String> getSortTypes();
}
