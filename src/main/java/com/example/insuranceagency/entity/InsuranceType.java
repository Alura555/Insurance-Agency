package com.example.insuranceagency.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class InsuranceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private boolean isActive;

    @Formula("(SELECT COUNT(*) FROM Offer o WHERE o.insurance_type_id = id AND o.is_active = true)")
    private Integer activeOffersCount;

    public InsuranceType(String title, boolean isActive, Integer activeOffersCount) {
        this.title = title;
        this.isActive = isActive;
        this.activeOffersCount = activeOffersCount;
    }

    public InsuranceType() {
    }


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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getActiveOffersCount() {
        return activeOffersCount;
    }

    public void setActiveOffersCount(Integer activeOffersCount) {
        this.activeOffersCount = activeOffersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        InsuranceType that = (InsuranceType) o;

        if (isActive != that.isActive)
            return false;
        if (!title.equals(that.title))
            return false;
        return Objects.equals(activeOffersCount, that.activeOffersCount);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (isActive ? 1 : 0);
        result = 31 * result + (activeOffersCount != null ? activeOffersCount.hashCode() : 0);
        return result;
    }
}
