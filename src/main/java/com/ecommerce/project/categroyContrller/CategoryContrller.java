package com.ecommerce.project.categroyContrller;
import com.ecommerce.project.CategoryModel.Category;
import com.ecommerce.project.categoryDTO.CategoryDTO;
import com.ecommerce.project.categoryDTO.GetAllCategoryResponse;
import com.ecommerce.project.categoryDTO.ResponseDTO;
import com.ecommerce.project.categoryService.CategoryService;
import com.ecommerce.project.categoryconfig.AppConstant;
import com.ecommerce.project.categoryconfig.Appconfig;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
public class  CategoryContrller {

    private  final CategoryService categoryService;

    public CategoryContrller(CategoryService categoryService) {
        this.categoryService = categoryService;
    }



    @GetMapping("/api/public/categories")
    ResponseEntity<GetAllCategoryResponse>getAllcategory(

            @RequestParam(name = "pageNumber",defaultValue = AppConstant.page_Number ,required = false) Integer pageNumber ,
            @RequestParam(name = "pageSize",defaultValue = AppConstant.page_Size ,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstant.sort_Category ,required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstant.sort_DIR ,required = false) String sortOrder

                                                         ){
      GetAllCategoryResponse fethcagory= categoryService.getAllCategory(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(fethcagory,HttpStatus.OK);
    }



    @PostMapping("/api/public/categories")
    ResponseEntity<ResponseDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        ResponseDTO createdto= categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdto,HttpStatus.OK);
    }


    @DeleteMapping("/api/admin/categories/{categoryId}")
    ResponseEntity<ResponseDTO> deleteCategory(@PathVariable Long categoryId){
      ResponseDTO responseDTO=  categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }


    @PutMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<ResponseDTO> updateCategories(@RequestBody  CategoryDTO category,
                                                        @PathVariable Long categoryId){
        try{
            ResponseDTO status=categoryService.updateCategory(category,categoryId);
            return new ResponseEntity<>(status,HttpStatus.OK);

        }catch (ResponseStatusException e){
            return ResponseEntity.badRequest().build();
        }

    }


}







