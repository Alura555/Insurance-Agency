package com.gitlab.alura.insuranceagency.dto;

import com.gitlab.alura.insuranceagency.entity.Document;
import com.gitlab.alura.insuranceagency.entity.DocumentType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;

public class PolicyDto {
    private Long id;
    private String client;
    private String manager;
    private OfferDto offer;
    private Date creationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiredDate;
    private int periodInMonths;
    private Map<DocumentType, Document> documents;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public OfferDto getOffer() {
        return offer;
    }

    public void setOffer(OfferDto offer) {
        this.offer = offer;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public int getPeriodInMonths() {
        return periodInMonths;
    }

    public void setPeriodInMonths(int periodInMonths) {
        this.periodInMonths = periodInMonths;
    }

    public Map<DocumentType, Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<DocumentType, Document> documents) {
        this.documents = documents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
