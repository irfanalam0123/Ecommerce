package com.ecommerce.project.categoryService;

import com.ecommerce.project.CategoryModel.Category;
import com.ecommerce.project.categoryDTO.CategoryDTO;
import com.ecommerce.project.categoryDTO.GetAllCategoryResponse;
import com.ecommerce.project.categoryDTO.ResponseDTO;
import com.ecommerce.project.categoryRepository.CategoryRepository;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImp implements CategoryService {


        @Autowired
        private  CategoryRepository categoryRepository;
        @Autowired
        private ModelMapper modelMapper;
        // private  long indexId=1L;
    public CategoryServiceImp(CategoryRepository categoryRepository) {
            this.categoryRepository = categoryRepository;
        }

        @Override
        public  ResponseDTO createCategory(CategoryDTO categoryDTO) {
         Category category=modelMapper.map(categoryDTO,Category.class);
            Category exitCategory=categoryRepository.findByCategoryName(category.getCategoryName());
            if(exitCategory != null)
                throw new APIException("catory with name " +
                        category.getCategoryName()+"is already exit !!!");

            Category saveCategory=categoryRepository.save(category);
            return modelMapper.map(saveCategory,ResponseDTO.class);

    }


    @Override
    public GetAllCategoryResponse getAllCategory(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sortbyOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortbyOrder);
        Page<Category> pageCategory=categoryRepository.findAll(pageDetails);

        List<Category> categories=pageCategory.getContent();
        if(categories.isEmpty())
            throw new APIException("catogory is not avilable");

        List<ResponseDTO>tempres=categories.stream()
                .map(category ->modelMapper.map(category,ResponseDTO.class)).toList();

        GetAllCategoryResponse response=new GetAllCategoryResponse();
        response.setContent(tempres);
        response.setPageNumber(pageCategory.getNumber());
        response.setPageSize(pageCategory.getSize());
        response.setTotalElements(pageCategory.getTotalElements());
        response.setTotalPage(pageCategory.getTotalPages());
        response.setLastPage(pageCategory.isLast());
        return response;

    }



    @Override
    public ResponseDTO deleteCategory(Long categoryId) {
        Category categories=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("category","categoryId",categoryId));
        categoryRepository.delete(categories);
        return modelMapper.map(categories,ResponseDTO.class);

    }



    @Override
    public ResponseDTO  updateCategory(CategoryDTO categoryDTO, Long categoryId) {

        Category category1=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("category","categoryId",categoryId));

        Category category=modelMapper.map(categoryDTO,Category.class);
        category1.setCategoryName(category.getCategoryName());
          Category save= categoryRepository.save(category1);
          return modelMapper.map(save,ResponseDTO.class);

    }
}
