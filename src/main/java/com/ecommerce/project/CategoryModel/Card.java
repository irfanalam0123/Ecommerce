package com.ecommerce.project.CategoryModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cards")
public class Card {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private  User user;


    @OneToMany(mappedBy = "card",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = false)
    private List<CardItem>cardItems=new ArrayList<>();

    private  double totalPrice=0.0;
}
