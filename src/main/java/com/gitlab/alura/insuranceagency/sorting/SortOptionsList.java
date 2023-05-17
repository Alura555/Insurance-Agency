package com.gitlab.alura.insuranceagency.sorting;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SortOptionsList {
    private final List<SortOption> sortOptions = new ArrayList<>();

    public SortOptionsList() {
        sortOptions.add(new SortOption("Best sellers", Sort.by("approvedPolicyCount").descending()));
        sortOptions.add(new SortOption("Price: High to low", Sort.by("price").descending()));
        sortOptions.add(new SortOption("Price: Low to high", Sort.by("price")));
        sortOptions.add(new SortOption("Title: A - Z", Sort.by("title")));
        sortOptions.add(new SortOption("Title: Z - A", Sort.by("title").descending()));
    }

    public List<String> getSortOptionNames() {
        return sortOptions.stream()
                .map(SortOption::getName)
                .collect(Collectors.toList());
    }

    public Sort getSortByIndex(int sortType) {
        return sortOptions.get(sortType).getSort();
    }
}
