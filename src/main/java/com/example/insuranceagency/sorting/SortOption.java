package com.example.insuranceagency.sorting;

import org.springframework.data.domain.Sort;

public class SortOption {
    private String name;
    private Sort sort;

    public SortOption(String name, Sort sort) {
        this.name = name;
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}