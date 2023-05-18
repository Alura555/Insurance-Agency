package com.gitlab.alura.insuranceagency.service.implementation;

import com.gitlab.alura.insuranceagency.repository.OfferRepository;
import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.entity.Offer;
import com.gitlab.alura.insuranceagency.filter.OfferFilter;
import com.gitlab.alura.insuranceagency.mapper.OfferMapper;
import com.gitlab.alura.insuranceagency.service.OfferService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    private final OfferMapper offerMapper;

    public OfferServiceImpl(OfferRepository offerRepository,
                            OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    @Override
    public Page<OfferDto> findAll(Pageable pageable, OfferFilter offerFilter) {
        Page<Offer> offerPage = offerRepository.findAll(offerFilter, pageable);
        List<OfferDto> offerDtoList = offerPage
                .getContent()
                .stream()
                .map(offerMapper::toOfferDto)
                .collect(Collectors.toList());
        return new PageImpl<>(offerDtoList, pageable, offerPage.getTotalElements());
    }

    @Override
    public OfferDto findDtoById(Long id) {
        Offer offer = findById(id);
        return offerMapper.toOfferDto(offer);
    }

    @Override
    public Offer findById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }

    public BigDecimal getMaxPrice(){
        return offerRepository.findTopByOrderByPriceDesc().getPrice();
    }

    public BigDecimal getMinPrice(){
        return offerRepository.findTopByOrderByPrice().getPrice();
    }
}
