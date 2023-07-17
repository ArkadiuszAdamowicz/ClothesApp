package com.example.clothesapp.service;


import com.example.clothesapp.model.Category;
import com.example.clothesapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Value("${app.categories}")
    private String[] categoryNames;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void initCategories() {
        for (String name : categoryNames) {
            if (categoryRepository.findByName(name) == null) {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
            }
        }
    }


    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}