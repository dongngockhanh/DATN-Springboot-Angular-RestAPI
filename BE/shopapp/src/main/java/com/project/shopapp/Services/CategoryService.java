package com.project.shopapp.Services;

import com.project.shopapp.DTOs.CategoryDTO;
import com.project.shopapp.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(long id);
    List<Category> getAllCategory();
    Category updateCategory(long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(long id);
}
