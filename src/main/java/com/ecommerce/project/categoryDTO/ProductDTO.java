package com.ecommerce.project.categoryDTO;

import com.ecommerce.project.CategoryModel.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private  Long productId;
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private double discount;
    private double price;
    private double specialPrice;
    private Category category;
}
