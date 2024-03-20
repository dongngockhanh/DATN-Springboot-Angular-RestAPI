package com.project.shopapp.Services;

import com.project.shopapp.DTOs.ProductDTO;
import com.project.shopapp.DTOs.ProductImageDTO;
import com.project.shopapp.DTOs.responses.ProductResponse;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface ProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product getProductById(long id) throws DataNotFoundException;
    Product updateProduct(long id,ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(long id);
    boolean existProductByName(String name);
    ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception;
}
