package com.ecommerce.project.categoryService;

import com.ecommerce.project.categoryDTO.ProductDTO;
import com.ecommerce.project.categoryDTO.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {


    ProductDTO addproduct(ProductDTO productDTO, Long categoryId);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO delteProduct(Long productId);

    ProductDTO uploadedProductImage(Long productId, MultipartFile images) throws IOException;

    ProductResponse getAllproduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchBycategory(Long categoryId, Integer pageNumber,
                                     Integer pageSize, String sortBy, String sortOrder);


    ProductResponse searchkeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
