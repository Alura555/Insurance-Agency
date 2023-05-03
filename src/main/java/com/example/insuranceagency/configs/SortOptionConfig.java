package com.example.insuranceagency.configs;

import com.example.insuranceagency.sorting.SortOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SortOptionConfig {
    @Bean
    public List<SortOption> getSortOptionList(){
        List<SortOption> sortOptions = new ArrayList<>();

        sortOptions.add(new SortOption("Best sellers", Sort.by("approvedPolicyCount").descending()));
        sortOptions.add(new SortOption("Price: High to low", Sort.by("price").descending()));
        sortOptions.add(new SortOption("Price: Low to high", Sort.by("price")));
        sortOptions.add(new SortOption("Title: A - Z", Sort.by("title")));
        sortOptions.add(new SortOption("Title: Z - A", Sort.by("title").descending()));

        return sortOptions;
    }
}
