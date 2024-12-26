package com.example.financialmanager.storage;

import com.example.financialmanager.model.Category;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class CategoryStorage {
    private final FileStorage<Category> fileStorage;

    public CategoryStorage() {
        this.fileStorage = new FileStorage<Category>("src/data/categories.json");
    }

    public List<Category> getAllCategories() throws IOException {
        return fileStorage.getAll(Category.class); // Передаем тип Category для десериализации
    }

    public void addCategory(Category category) throws IOException {
        fileStorage.add(category);
    }

    public void deleteCategory(Predicate<Category> predicate) throws IOException {
        fileStorage.delete(predicate);
    }

    public void updateCategory(Predicate<Category> predicate, Category updatedCategory) throws IOException {
        fileStorage.update(predicate, updatedCategory);
    }
}
