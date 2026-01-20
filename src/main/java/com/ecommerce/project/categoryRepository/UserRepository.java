package com.ecommerce.project.categoryRepository;

import com.ecommerce.project.CategoryModel.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

 Optional<User>findByUserName(String username);
 boolean existsByUserName(@NotBlank String username);
 boolean existsByEmail(@NotBlank String username);
}
