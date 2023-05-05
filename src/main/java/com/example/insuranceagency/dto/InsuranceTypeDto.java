package com.example.insuranceagency.dto;

public class InsuranceTypeDto {

    private Long id;

    private String title;

    public InsuranceTypeDto() {
    }

    public InsuranceTypeDto(String title) {
        this.title = title;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        InsuranceTypeDto that = (InsuranceTypeDto) o;

        return title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
