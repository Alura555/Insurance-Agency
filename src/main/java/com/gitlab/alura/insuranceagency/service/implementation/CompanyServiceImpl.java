package com.gitlab.alura.insuranceagency.service.implementation;

import com.gitlab.alura.insuranceagency.dto.CompanyDto;
import com.gitlab.alura.insuranceagency.entity.User;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.mapper.CompanyMapper;
import com.gitlab.alura.insuranceagency.repository.CompanyRepository;
import com.gitlab.alura.insuranceagency.entity.Company;
import com.gitlab.alura.insuranceagency.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository,
                              CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
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
        Company company = getCompanyById(id);
        return companyMapper.toDto(company);
    }

    @Override
    public Long createCompany(CompanyDto companyDto) {
        logger.info("Creating new company: {}", companyDto.getName());
        Company company = companyMapper.toEntity(companyDto);
        company.setActive(true);
        Long companyId = companyRepository.save(company).getId();

        logger.info("New company created with ID: {}", companyId);
        return companyId;
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting company with ID: {}", id);
        Company company = getCompanyById(id);
        company.setActive(false);
        companyRepository.save(company);
        logger.info("Company with ID: {} deleted successfully", id);
    }

    @Override
    public Long updateCompany(CompanyDto companyDto) {
        Company oldCompany = getCompanyById(companyDto.getId());
        Set<User> managers = oldCompany.getManagers();
        deleteById(companyDto.getId());

        Company newCompany = companyMapper.toEntity(companyDto);
        newCompany.setActive(true);
        newCompany.setId(null);
        newCompany.setManagers(new HashSet<>(managers));
        return companyRepository.save(newCompany).getId();
    }

    @Override
    public void addCompanyManager(User manager, Long companyId) {
        Company company = getCompanyById(companyId);
        company.getManagers().add(manager);
        companyRepository.save(company);
        logger.info("Added company manager for manager with email: {} and companyId: {}", manager.getEmail(), companyId);
    }

    private Company getCompanyById(Long companyId) {
        return companyRepository.findByIdAndIsActive(companyId, true)
                .orElseThrow(NotFoundException::new);
    }
}
