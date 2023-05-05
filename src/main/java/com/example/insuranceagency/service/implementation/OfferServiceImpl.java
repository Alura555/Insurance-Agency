package com.example.insuranceagency.service.implementation;

import com.example.insuranceagency.dto.OfferDto;
import com.example.insuranceagency.entity.Offer;
import com.example.insuranceagency.filter.OfferFilter;
import com.example.insuranceagency.mapper.OfferMapper;
import com.example.insuranceagency.repository.OfferRepository;
import com.example.insuranceagency.service.OfferService;
import com.example.insuranceagency.sorting.SortOptionsList;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    private final SortOptionsList sortOptions;

    private final OfferMapper offerMapper;

    public OfferServiceImpl(OfferRepository offerRepository,
                            SortOptionsList sortOptions,
                            OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.sortOptions = sortOptions;
        this.offerMapper = offerMapper;
    }

    public List<String> getSortTypes(){
        return sortOptions.getSortOptionNames();
    }

    @Override
    public Page<OfferDto> findAll(int page, int size, OfferFilter offerFilter, int sortType) {
        Sort sortOption = sortOptions.getSortByIndex(sortType - 1);
        Pageable pageable = PageRequest.of(page, size, sortOption);
        Page<Offer> offerPage = offerRepository.findAll(offerFilter, pageable);
        List<OfferDto> offerDtoList = offerPage
                .getContent()
                .stream()
                .map(offerMapper::toOfferDto)
                .collect(Collectors.toList());
        return new PageImpl<>(offerDtoList, pageable, offerPage.getTotalElements());
    }

    public OfferDto findById(Long id) {
        Offer offer = offerRepository.findById(id).orElse(null);
        return offerMapper.toOfferDto(offer);
    }

    public BigDecimal getMaxPrice(){
        return offerRepository.findTopByOrderByPriceDesc().getPrice();
    }

    public BigDecimal getMinPrice(){
        return offerRepository.findTopByOrderByPrice().getPrice();
    }
}
