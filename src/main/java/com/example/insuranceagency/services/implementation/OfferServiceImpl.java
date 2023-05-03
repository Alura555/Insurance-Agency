package com.example.insuranceagency.services.implementation;

import com.example.insuranceagency.entities.Offer;
import com.example.insuranceagency.filters.OfferFilter;
import com.example.insuranceagency.repositories.OfferRepository;
import com.example.insuranceagency.services.OfferService;
import com.example.insuranceagency.sorting.SortOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private List<SortOption> sortOptions;

    public List<String> getSortTypes(){
        return sortOptions
                .stream()
                .map(SortOption::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Offer> findAll(int page, int size, OfferFilter offerFilter, int sortType) {
        SortOption sortOption = sortOptions.get(sortType);
        List<Offer> offerList = offerRepository.findAll();
        Pageable pageable = PageRequest.of(page, size, sortOption.getSort());
        return offerRepository.findAll(offerFilter, pageable);
    }

    public Offer findById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }

    public Integer getMaxPrice(){
        return offerRepository.findTopByOrderByPriceDesc().getPrice().intValue();
    }

    public Integer getMinPrice(){
        return offerRepository.findTopByOrderByPrice().getPrice().intValue();
    }
}
