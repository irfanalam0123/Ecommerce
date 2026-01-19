package com.ecommerce.project.categroyContrller;

import com.ecommerce.project.CategoryModel.Product;
import com.ecommerce.project.categoryDTO.ProductDTO;
import com.ecommerce.project.categoryDTO.ProductResponse;
import com.ecommerce.project.categoryService.ProductService;
import com.ecommerce.project.categoryconfig.AppConstant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/category/admin/{category_id}/product")
    public ResponseEntity<ProductDTO>addproduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long category_id){

        ProductDTO saveproductDTO=productService.addproduct(productDTO,category_id);
        return new ResponseEntity<>(saveproductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/product")
    public ResponseEntity<ProductResponse>getAllproduct(
            @RequestParam(value = "pageNumber",defaultValue = AppConstant.page_Number,required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstant.page_Size,required = false) Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstant.sort_Product,required = false) String sortBy,
            @RequestParam(value = "sortOrder",defaultValue = AppConstant.sort_DIR,required = false) String sortOrder
                                                        ){
        ProductResponse productResponse=productService.getAllproduct(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/category/{category_id}/product")
    public ResponseEntity<ProductResponse>getAllproductByCategory(@PathVariable Long category_id,
                                                                  @RequestParam(value = "pageNumber",defaultValue = AppConstant.page_Number,required = false) Integer pageNumber,
                                                                  @RequestParam(value = "pageSize",defaultValue = AppConstant.page_Size,required = false) Integer pageSize,
                                                                  @RequestParam(value = "sortBy",defaultValue = AppConstant.sort_Product,required = false) String sortBy,
                                                                  @RequestParam(value = "sortOrder",defaultValue = AppConstant.sort_DIR,required = false) String sortOrder
                                                                  ) {
        ProductResponse productResponse = productService.searchBycategory(category_id, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
        @PostMapping("/category/keyword/{keyword}")
        public ResponseEntity<ProductResponse>getAllproductByCategory(@PathVariable String keyword,
                                                                      @RequestParam(value = "pageNumber",defaultValue = AppConstant.page_Number,required = false) Integer pageNumber,
                                                                      @RequestParam(value = "pageSize",defaultValue = AppConstant.page_Size,required = false) Integer pageSize,@RequestParam(value = "sortBy",defaultValue = AppConstant.sort_Product,required = false) String sortBy,
                                                                      @RequestParam(value = "sortOrder",defaultValue = AppConstant.sort_DIR,required = false) String sortOrder) {
            ProductResponse productResponse = productService.searchkeyword(keyword, pageNumber, pageSize,
                    sortBy, sortOrder);
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        }



        @PutMapping("/product/{productId}")
    public ResponseEntity<ProductDTO>updateProduct(@RequestBody ProductDTO productDTO,@PathVariable  Long productId){
        ProductDTO productResponse=productService.updateProduct(productDTO,productId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }



    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO>deleteProduct(@PathVariable  Long productId){
        ProductDTO productResponse=productService.delteProduct(productId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @PutMapping("/admin/product/image/{productId}")
    public ResponseEntity<ProductDTO>uploadImage(@PathVariable  Long productId,@RequestParam("image")
    MultipartFile images) throws IOException {
        ProductDTO productResponse=productService.uploadedProductImage(productId,images);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

}
