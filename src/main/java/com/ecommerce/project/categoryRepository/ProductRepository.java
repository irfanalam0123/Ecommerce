package com.ecommerce.project.categoryRepository;

import com.ecommerce.project.CategoryModel.Category;
import com.ecommerce.project.CategoryModel.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByCategoryOrderByPrice(Category category, Pageable pageDetails);
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageDetails);
}
