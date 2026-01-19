package com.ecommerce.project.CategoryModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5,message = "street at least the 5 char")
    private String street;

    @NotBlank
    @Size(min = 5,message = "building at least the 5 char")
    private String buildingName;

    @NotBlank
    @Size(min = 5,message = "city at least the 5 char")
    private String city;

    @NotBlank
    @Size(min = 5,message = "state at least the 5 char")
    private String state;

    @NotBlank
    @Size(min = 5,message = "country at least the 5 char")
    private String country;

    @NotBlank
    @Size(min = 5,message = "pincode at least the 5 char")
    private  String pincode;


   @ManyToMany(mappedBy = "addresses")
   private Set<User>users=new HashSet<>();

    public Address(String street, String buildingName,
                   String city, String state, String country,
                   String pincode, Set<User> users) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.users = users;
    }

}
