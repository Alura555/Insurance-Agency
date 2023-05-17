package com.gitlab.alura.insuranceagency.filter;

import com.gitlab.alura.insuranceagency.entity.Offer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class OfferFilter implements Specification<Offer> {
    private Integer maxPrice;
    private Integer minPrice;
    private Boolean isActive;
    private Long company;
    private Long insuranceType;
    private String searchQuery;

    @Override
    public Predicate toPredicate(Root<Offer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (minPrice != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        if (company != null && company != 0L) {
            predicates.add(criteriaBuilder.equal(root.get("company").get("id"), company));
        }
        if (insuranceType != null && insuranceType != 0L) {
            predicates.add(criteriaBuilder.equal(root.get("insuranceType").get("id"), insuranceType));
        }
        if (searchQuery != null && !searchQuery.equals("")) {
            Predicate titleContaining = criteriaBuilder.like(root.get("title"), "%" + searchQuery + "%");
            Predicate descriptionContaining = criteriaBuilder.like(root.get("description"), "%" + searchQuery + "%");
            Predicate typeContaining = criteriaBuilder.like(root.get("insuranceType").get("title"), "%" + searchQuery + "%");
            predicates.add(criteriaBuilder.or(titleContaining, descriptionContaining, typeContaining));
        }
        return predicates.isEmpty()
                ? null
                : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }

    public Long getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(Long insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
