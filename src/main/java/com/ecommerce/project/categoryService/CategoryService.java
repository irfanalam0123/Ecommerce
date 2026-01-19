package com.ecommerce.project.categoryService;

import com.ecommerce.project.CategoryModel.Category;
import com.ecommerce.project.categoryDTO.CategoryDTO;
import com.ecommerce.project.categoryDTO.GetAllCategoryResponse;
import com.ecommerce.project.categoryDTO.ResponseDTO;

import java.util.List;


public interface CategoryService {
    ResponseDTO createCategory(CategoryDTO categoryDTO);
    GetAllCategoryResponse getAllCategory(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    ResponseDTO deleteCategory(Long categoryId);
    ResponseDTO updateCategory(CategoryDTO category, Long categoryId);
}
