package com.ecommerce.project.categoryRepository;

import com.ecommerce.project.CategoryModel.AppRole;
import com.ecommerce.project.CategoryModel.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
   Optional<Role> findByRoleName(AppRole appRole);
}
