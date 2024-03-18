package com.project.shopapp.Services.Implement;

import com.project.shopapp.DTOs.CategoryDTO;
import com.project.shopapp.Repositories.CategoryRepository;
import com.project.shopapp.Services.CategoryService;
import com.project.shopapp.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor// tìm thuộc tính final sẽ tự tạo constructor tương ứng
public class CategoryServiceImp implements CategoryService {
//    @Autowired
    private final CategoryRepository categoryRepository;

//    public CategoryServiceImp(CategoryRepository categoryRepository){
//        this.categoryRepository = categoryRepository;
//    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = Category    //convert từ DTO sang entity
                .builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("không tìm thấy danh mục"));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryId,CategoryDTO categoryDTO) {
        Category existingCategory =getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
