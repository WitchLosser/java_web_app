package org.example.DAOs;

import org.example.entities.Category;

import java.util.List;

public interface ICategoryDao {

    Category getCategoryById(int id);

    List<Category> getAllCategories();

    void createCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(Category category);

}