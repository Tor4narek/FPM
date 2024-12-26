package com.example.financialmanager.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileStorage<T> {
    private final String filePath;
    private final ObjectMapper objectMapper;

    public FileStorage(String filePath) {
        this.filePath = filePath;
        this.objectMapper = new ObjectMapper();
    }

    private List<T> loadData(Class<T> clazz) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>(); // Если файла нет, возвращаем пустой список
        }

        // Используем TypeFactory для десериализации в нужный тип (например, Category)
        return objectMapper.readValue(file, TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
    }

    private void saveData(List<T> data) throws IOException {
        objectMapper.writeValue(new File(filePath), data);
    }

    public List<T> getAll(Class<T> clazz) throws IOException {
        return loadData(clazz);
    }

    public void add(T entity) throws IOException {
        List<T> data = loadData((Class<T>) entity.getClass()); // Получаем класс из объекта
        data.add(entity);
        saveData(data);
    }

    public void delete(java.util.function.Predicate<T> predicate) throws IOException {
        List<T> data = loadData((Class<T>) predicate.getClass());
        data.removeIf(predicate);
        saveData(data);
    }

    public void update(java.util.function.Predicate<T> predicate, T updatedEntity) throws IOException {
        List<T> data = loadData((Class<T>) updatedEntity.getClass());
        for (int i = 0; i < data.size(); i++) {
            if (predicate.test(data.get(i))) {
                data.set(i, updatedEntity);
                break;
            }
        }
        saveData(data);
    }
}
