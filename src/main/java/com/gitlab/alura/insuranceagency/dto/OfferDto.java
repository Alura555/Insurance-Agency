package com.gitlab.alura.insuranceagency.dto;

import com.gitlab.alura.insuranceagency.entity.DocumentType;

import java.math.BigDecimal;
import java.util.Set;

public class OfferDto {
    private Long id;
    private String title;
    private String period;
    private int months;
    private int years;
    private String description;
    private String formattedPrice;
    private BigDecimal price;
    private Boolean isActive;
    private String companyName;
    private InsuranceTypeDto insuranceType;
    private Set<DocumentType> documents;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public InsuranceTypeDto getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(InsuranceTypeDto insuranceType) {
        this.insuranceType = insuranceType;
    }

    public Set<DocumentType> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<DocumentType> documents) {
        this.documents = documents;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }
}
