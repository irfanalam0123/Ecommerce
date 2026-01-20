package com.ecommerce.project.categoryService;


import com.ecommerce.project.CategoryModel.Category;
import com.ecommerce.project.CategoryModel.Product;
import com.ecommerce.project.categoryDTO.ProductDTO;
import com.ecommerce.project.categoryDTO.ProductResponse;
import com.ecommerce.project.categoryRepository.CategoryRepository;
import com.ecommerce.project.categoryRepository.ProductRepository;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductImp implements ProductService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public ProductDTO addproduct(ProductDTO productDTO, Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("categoru","categoryId",categoryId));

        boolean isPresent=true;

        List<Product>products= category.getProduct();

        for (Product value : products){
            if(value.getProductName().equals(productDTO.getProductName())){
                isPresent=false;
                break;
            }
        }


       if(isPresent) {
           Product product = modelMapper.map(productDTO, Product.class);
           product.setProductId(product.getProductId());
           product.setProductName(product.getProductName());
           double specialPrice =
                   product.getPrice() - (product.getPrice() * product.getDiscount() / 100);

           product.setSpecialPrice(specialPrice);
           product.setImage("default.jpg");
           product.setCategory(category);

           Product savedProduct = productRepository.save(product);
           return modelMapper.map(savedProduct, ProductDTO.class);
       }else {
           throw new APIException("prduct is alredy exit");
       }
    }

    @Override
    public ProductResponse getAllproduct(Integer pageNumber, Integer pageSize,
                                         String sortBy, String sortOrder) {
        Sort sort=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product>pageall=productRepository.findAll(pageDetails);
        List<Product>products =pageall.getContent();

        List<ProductDTO>productDTOS=products.stream()
                .map((product)->modelMapper.map(product, ProductDTO.class)).toList();

//        if(products.isEmpty()){
//            return products;
//           // throw new APIException("not any product exit!!");
//        }

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageNumber);
        productResponse.setPageSize(pageSize);
        productResponse.setTotalPage(pageall.getTotalPages());
        productResponse.setTotalElements(pageall.getTotalElements());
        productResponse.setLastPage(productResponse.isLastPage());

        return productResponse;
    }
    @Override
    public ProductResponse searchBycategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("categoru","categoryId",categoryId));

        Sort sort=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product>pageall=productRepository.findByCategoryOrderByPrice(category,pageDetails);
        List<Product>products =pageall.getContent();

       // List<Product>products =productRepository.findByCategoryOrderByPrice(category);
        List<ProductDTO>productDTOS=products.stream()
                .map((product)->modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageNumber);
        productResponse.setPageSize(pageSize);
        productResponse.setTotalPage(pageall.getTotalPages());
        productResponse.setTotalElements(pageall.getTotalElements());
        productResponse.setLastPage(pageall.isLast());
        return productResponse;

    }

    @Override
    public ProductResponse searchkeyword(String keyword, Integer pageNumber, Integer pageSize,
                                         String sortBy, String sortOrder) {

        Sort sort=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product>pageall=  productRepository.
                findByProductNameContainingIgnoreCase(keyword,pageDetails);
        List<Product>products =pageall.getContent();

//        List<Product> products =
//                productRepository.findByProductNameContainingIgnoreCase(keyword);

        List<ProductDTO>productDTOS=products.stream()
                .map((product)->modelMapper.map(product, ProductDTO.class)).toList();


        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageNumber);
        productResponse.setTotalPage(pageall.getTotalPages());
        productResponse.setTotalElements(pageall.getTotalElements());
        productResponse.setLastPage(pageall.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct( ProductDTO productDTO, Long productId) {
        Product findProduct=productRepository.findById(productId)
                     .orElseThrow(()->new ResourceNotFoundException("productid","productid",+productId));

  Product product=modelMapper.map(productDTO, Product.class);
        findProduct.setProductName(product.getProductName());
        findProduct.setQuantity(product.getQuantity());
        findProduct.setDescription(product.getDescription());
        findProduct.setPrice(product.getPrice());
        findProduct.setSpecialPrice(product.getSpecialPrice());
       Product saves= productRepository.save(findProduct);

        return modelMapper.map(saves, ProductDTO.class);
    }

    @Override
    public ProductDTO delteProduct(Long productId) {
        Product findProduct=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("productid","productid",+productId));


        productRepository.delete(findProduct);

        return modelMapper.map(findProduct, ProductDTO.class);
    }


    @Override
    public ProductDTO uploadedProductImage(Long productId, MultipartFile images) throws IOException {
        Product findProduct=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("productid","productid",+productId));

       // String path="/images";
        String path = System.getProperty("user.dir") + File.separator + "images";

        String fileName=uplodedImage(path,images);
         findProduct.setImage(fileName);
     Product save=productRepository.save(findProduct);

        return modelMapper.map(save, ProductDTO.class);
    }





    private String uplodedImage(String path, MultipartFile file) throws IOException {

        String orinalFilename=file.getOriginalFilename();
        String imageid= UUID.randomUUID().toString();
        String newfiles=imageid.concat(orinalFilename.substring(orinalFilename.lastIndexOf('.')));
        String filepath=path +File.separator+newfiles;
        File folder=new File(path);
        if(!folder.exists()) folder.mkdir();
        Files.copy(file.getInputStream(), Paths.get(filepath));

        return newfiles;
    }

}
