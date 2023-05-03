package com.example.insuranceagency.dtos;

public class OfferDto {
    private Long id;
    private String title;
    private String period;
    private String description;
    private String price;
    private Boolean isActive;
    private String companyName;
    private InsuranceTypeDto insuranceType;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
