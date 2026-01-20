package com.ecommerce.project.CategoryModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "card_items")
public class CardItem {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long cardItemId;
    @ManyToOne
    @JoinColumn(name = "card_id")
    private  Card card;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    private Integer quantity;
    private double discount;
    private double productPrice;
}
