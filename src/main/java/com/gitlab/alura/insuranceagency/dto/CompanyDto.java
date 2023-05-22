package com.gitlab.alura.insuranceagency.dto;

import java.util.Set;

public class CompanyDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Boolean isActive;
    private Set<UserDto> managers;

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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Set<UserDto> getManagers() {
        return managers;
    }

    public void setManagers(Set<UserDto> managers) {
        this.managers = managers;
    }
}
