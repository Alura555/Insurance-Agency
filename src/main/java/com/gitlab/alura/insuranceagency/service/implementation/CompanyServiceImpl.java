package com.gitlab.alura.insuranceagency.service.implementation;

import com.gitlab.alura.insuranceagency.dto.CompanyDto;
import com.gitlab.alura.insuranceagency.entity.User;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.mapper.CompanyMapper;
import com.gitlab.alura.insuranceagency.repository.CompanyRepository;
import com.gitlab.alura.insuranceagency.entity.Company;
import com.gitlab.alura.insuranceagency.service.CompanyService;
import com.gitlab.alura.insuranceagency.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    private final UserService userService;
    public CompanyServiceImpl(CompanyRepository companyRepository,
                              CompanyMapper companyMapper,
                              UserService userService) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.userService = userService;
    }

    public List<CompanyDto> getActiveCompanies(){
        List<Company> companies = companyRepository.findAllByIsActive(true);
        return companies
                .stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Company> getPopularCompanies(int n) {
        if (n <= 0){
            throw new IllegalArgumentException();
        }
        return companyRepository.findAllByIsActive(true)
                .stream()
                .sorted(Comparator.comparing(Company::getActiveOffersCount).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    @Override
    public Company getCompanyByManager(User user) {
        return companyRepository.findByManagers(user);
    }

    @Override
    public Page<CompanyDto> getAllActive(Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAllByIsActive(pageable, true);
        List<CompanyDto> companyDtoList = companyPage
                .getContent()
                .stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(companyDtoList, pageable, companyPage.getTotalElements());
    }

    @Override
    public CompanyDto getCompanyDtoById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(NotFoundException::new);
        return companyMapper.toDto(company);
    }

    @Override
    public Long createNewCompany(CompanyDto companyDto) {
        Company company = companyMapper.toEntity(companyDto);
        company.setActive(true);
        return companyRepository.save(company).getId();
    }

    @Override
    public void deleteById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(NotFoundException::new);
        company.setActive(false);
        companyRepository.save(company);
    }

    @Override
    public Long updateCompany(CompanyDto companyDto) {
        Company oldCompany = companyRepository.findById(companyDto.getId()).orElseThrow(NotFoundException::new);
        Set<User> managers = oldCompany.getManagers();
        deleteById(companyDto.getId());

        Company newCompany = companyMapper.toEntity(companyDto);
        newCompany.setActive(true);
        newCompany.setId(null);
        newCompany.setManagers(new HashSet<>(managers));
        return companyRepository.save(newCompany).getId();
    }

    @Override
    public void addCompanyManager(Long managerId, Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(NotFoundException::new);
        company.getManagers().add(userService.findById(managerId));
        companyRepository.save(company);
    }


}
