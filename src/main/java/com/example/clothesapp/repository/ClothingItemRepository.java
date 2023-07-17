package com.example.clothesapp.repository;

import com.example.clothesapp.model.ClothingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClothingItemRepository extends JpaRepository<ClothingItem, Long> {
    List<ClothingItem> findByCategoryId(Long categoryId);
}
