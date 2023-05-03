package com.example.insuranceagency.entities;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer periodInMonths;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_type_id", nullable = false)
    private InsuranceType insuranceType;

    @ManyToMany
    @JoinTable(
            name = "required_documents",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "document_type_id"))
    private Set<DocumentType> documents;

    @Formula("(SELECT COUNT(*) FROM Policies p WHERE p.offer_id = id AND p.is_approved = true)")
    private Integer approvedPolicyCount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public Set<DocumentType> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<DocumentType> documents) {
        this.documents = documents;
    }

    public Integer getPeriodInMonths() {
        return periodInMonths;
    }

    public void setPeriodInMonths(Integer periodInMonths) {
        this.periodInMonths = periodInMonths;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Integer getApprovedPolicyCount() {
        return approvedPolicyCount;
    }

    public void setApprovedPolicyCount(Integer approvedPolicyCount) {
        this.approvedPolicyCount = approvedPolicyCount;
    }
}
