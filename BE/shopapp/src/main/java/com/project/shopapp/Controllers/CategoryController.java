package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.CategoryDTO;
import com.project.shopapp.Services.CategoryService;
import com.project.shopapp.Services.Implement.CategoryServiceImp;
import com.project.shopapp.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@Validated
@RequiredArgsConstructor
@RequestMapping("${api.basePath}/categories")
public class CategoryController {
//    private final CategoryServiceImp categoryServiceImp;
    private final CategoryService categoryService;
    // thêm danh mục
    @PostMapping("")
    public ResponseEntity<?>createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result)
    {
        if(result.hasErrors())
        {
            List<String> errorMessages =  result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);

        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert Category successfully "+categoryDTO);
    }


    @GetMapping("")// http://localhost:8888/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<Category>> getAllCategory(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    )
    {
        List<Category> categories= categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable long id,@Valid @RequestBody CategoryDTO categoryDTO)
    {
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("update category successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id)
    {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("delete category with "+id+" successfully");
    }
}
