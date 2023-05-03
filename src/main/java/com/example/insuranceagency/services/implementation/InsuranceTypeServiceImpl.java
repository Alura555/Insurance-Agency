package com.example.insuranceagency.services.implementation;

import com.example.insuranceagency.dtos.InsuranceTypeDto;
import com.example.insuranceagency.entities.Company;
import com.example.insuranceagency.entities.InsuranceType;
import com.example.insuranceagency.mappers.InsuranceTypeMapper;
import com.example.insuranceagency.repositories.InsuranceTypeRepository;
import com.example.insuranceagency.services.InsuranceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceTypeServiceImpl implements InsuranceTypeService {
    @Autowired
    private InsuranceTypeRepository insuranceTypeRepository;

    @Autowired
    private InsuranceTypeMapper insuranceTypeMapper;


    public List<InsuranceTypeDto> getInsuranceTypes(){
        return insuranceTypeRepository.findAllByIsActive(true)
                .stream()
                .map(x -> insuranceTypeMapper.toDto(x))
                .collect(Collectors.toList());
    }

    @Override
    public List<InsuranceTypeDto> getPopularInsuranceTypes(int n) {
        if (n <= 0){
            throw new IllegalArgumentException();
        }

        return insuranceTypeRepository.findAllByIsActive(true)
                .stream()
                .sorted(Comparator.comparing(InsuranceType::getActiveOffersCount).reversed())
                .limit(n)
                .map(x -> insuranceTypeMapper.toDto(x))
                .collect(Collectors.toList());
    }
}
