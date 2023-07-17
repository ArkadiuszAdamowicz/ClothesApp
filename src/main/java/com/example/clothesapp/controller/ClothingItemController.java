package com.example.clothesapp.controller;

import com.example.clothesapp.model.Category;
import com.example.clothesapp.model.ClothingItem;
import com.example.clothesapp.repository.ClothingItemRepository;
import com.example.clothesapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class ClothingItemController {
    private final ClothingItemRepository clothingItemRepository;
    private final CategoryService categoryService;

    @Autowired
    public ClothingItemController(ClothingItemRepository clothingItemRepository, CategoryService categoryService) {
        this.clothingItemRepository = clothingItemRepository;
        this.categoryService = categoryService;
    }


    @GetMapping("/clothing")
    public String listAllClothing(Model model, @RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            Long categoryId = Long.parseLong(category);
            model.addAttribute("items", clothingItemRepository.findByCategoryId(categoryId));
        } else {
            model.addAttribute("items", clothingItemRepository.findAll());
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        return "clothing";
    }

    @PostMapping("/clothing/add")
    public String addClothing(@ModelAttribute ClothingItem item, @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            item.setImagePath("/uploads/" + fileName);

            try {
                Path uploadDir = Paths.get("./uploads");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                Path filePath = uploadDir.resolve(fileName);
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        clothingItemRepository.save(item);
        return "redirect:/clothing";
    }

    @GetMapping("/clothing/add")
    public String getAddClothingForm(Model model) {
        model.addAttribute("item", new ClothingItem());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add-clothing";
    }


    @GetMapping("/clothing/edit/{id}")
    public String showEditClothingForm(@PathVariable("id") Long id, Model model) {
        ClothingItem item = clothingItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
        model.addAttribute("item", item);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-clothing";
    }

    @PostMapping("/clothing/edit/{id}")
    public String updateClothing(@PathVariable("id") Long id, @ModelAttribute ClothingItem item, @RequestParam("imageFile") MultipartFile imageFile, @RequestParam("categoryId") Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        item.setCategory(category);

        if (!imageFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            item.setImagePath("/uploads/" + fileName);

            try {
                Path uploadDir = Paths.get("./uploads");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                Path filePath = uploadDir.resolve(fileName);
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ClothingItem existingItem = clothingItemRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
            item.setImagePath(existingItem.getImagePath());
        }

        clothingItemRepository.save(item);
        return "redirect:/clothing";
    }

    @GetMapping("/clothing/delete/{id}")
    public String deleteClothing(@PathVariable("id") Long id) {
        ClothingItem item = clothingItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
        clothingItemRepository.delete(item);
        return "redirect:/clothing";
    }
}
