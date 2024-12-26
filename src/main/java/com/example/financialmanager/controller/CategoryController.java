package com.example.financialmanager.controller;

import com.example.financialmanager.model.Category;
import com.example.financialmanager.storage.CategoryStorage;

import java.io.IOException;
import java.util.List;

public class CategoryController {
    private final CategoryStorage categoryStorage;

    public CategoryController() {
        this.categoryStorage = new CategoryStorage();
    }

    // Получение всех категорий
    public List<Category> getAllCategories() throws IOException {
        return categoryStorage.getAllCategories();
    }

    // Добавление новой категории
    public void addCategory(String name, String description) throws IOException {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        categoryStorage.addCategory(category);
    }

    // Удаление категории по имени
    public void deleteCategory(String categoryName) throws IOException {
        categoryStorage.deleteCategory(category -> category.getName().equals(categoryName));
    }

    // Обновление категории
    public void updateCategory(String oldName, String newName, String newDescription) throws IOException {
        Category updatedCategory = new Category();
        updatedCategory.setName(newName);
        updatedCategory.setDescription(newDescription);
        categoryStorage.updateCategory(category -> category.getName().equals(oldName), updatedCategory);
    }
}
