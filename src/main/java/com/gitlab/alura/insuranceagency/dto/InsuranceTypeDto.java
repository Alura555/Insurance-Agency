package com.gitlab.alura.insuranceagency.dto;

public class InsuranceTypeDto {

    private Long id;

    private String formattedTitle;

    private String title;

    public InsuranceTypeDto() {
    }

    public InsuranceTypeDto(String title) {
        this.formattedTitle = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormattedTitle() {
        return formattedTitle;
    }

    public void setFormattedTitle(String formattedTitle) {
        this.formattedTitle = formattedTitle;
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

        return formattedTitle.equals(that.formattedTitle);
    }

    @Override
    public int hashCode() {
        return formattedTitle.hashCode();
    }

}
