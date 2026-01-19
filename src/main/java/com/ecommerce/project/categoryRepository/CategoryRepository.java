package com.ecommerce.project.categoryRepository;

import com.ecommerce.project.CategoryModel.Category;


import com.ecommerce.project.CategoryModel.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByCategoryName(String categoryName);
}
