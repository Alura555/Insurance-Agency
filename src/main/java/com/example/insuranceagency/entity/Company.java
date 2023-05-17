package com.example.insuranceagency.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToMany
    @JoinTable(
            name = "company_manager",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "manager_id"))
    private Set<User> managers;

    @Formula("(SELECT COUNT(*) FROM Offer o WHERE o.company_id = id AND o.is_active = true)")
    private Integer activeOffersCount;

    public Company() {
    }

    public Company(String name,
                   String email,
                   String phone,
                   String address,
                   Boolean isActive,
                   Set<User> managers,
                   Integer activeOffersCount) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.isActive = isActive;
        this.managers = managers;
        this.activeOffersCount = activeOffersCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getActiveOffersCount() {
        return activeOffersCount;
    }

    public void setActiveOffersCount(Integer approvedPolicyCount) {
        this.activeOffersCount = approvedPolicyCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Company company = (Company) o;

        if (!name.equals(company.name))
            return false;
        if (!email.equals(company.email))
            return false;
        if (!phone.equals(company.phone))
            return false;
        if (!address.equals(company.address))
            return false;
        if (!isActive.equals(company.isActive))
            return false;
        if (!Objects.equals(managers, company.managers))
            return false;
        return activeOffersCount.equals(company.activeOffersCount);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + isActive.hashCode();
        result = 31 * result + (managers != null ? managers.hashCode() : 0);
        result = 31 * result + activeOffersCount.hashCode();
        return result;
    }
}
