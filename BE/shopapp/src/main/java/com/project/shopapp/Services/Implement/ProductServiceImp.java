package com.project.shopapp.Services.Implement;

import com.project.shopapp.DTOs.ProductDTO;
import com.project.shopapp.DTOs.ProductImageDTO;
import com.project.shopapp.DTOs.reponses.ProductResponse;
import com.project.shopapp.Repositories.CategoryRepository;
import com.project.shopapp.Repositories.ProductImageRepository;
import com.project.shopapp.Repositories.ProductRepository;
import com.project.shopapp.Services.ProductService;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new DataNotFoundException("không tìm thấy danh mục với id = "+productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .image(productDTO.getImage())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // lấy danh sách sản phẩm theo page(trang) và limit(giới hạn sản phẩm trong trang)
       return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product getProductById(long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("không tìm thấy sản phẩm với id = "+id));
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
       Product existingProduct = getProductById(id);
       if(existingProduct != null) {
           //copy từ dto sang entity
           //sử dụng model mapper
           Category existingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                   .orElseThrow(()-> new DataNotFoundException("không tìm thấy danh mục với id = "+productDTO.getCategoryId()));
           existingProduct.setName(productDTO.getName());
           existingProduct.setPrice(productDTO.getPrice());
           existingProduct.setImage(productDTO.getImage());
           existingProduct.setDescription(productDTO.getDescription());
           existingProduct.setCategory(existingCategory);
           return productRepository.save(existingProduct);
       }
       return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id); //kiểm tra sự tồn tại của product
        optionalProduct.ifPresent(productRepository::delete);// thực hiện xoá nếu tồn tại bằng lamda optional
    }

    @Override
    public boolean existProductByName(String name) {
        return productRepository.existsByName(name);
    }

    // hàm thêm ảnh vào product
    @Override
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(()->
                new DataNotFoundException("không tìm thấy sản phẩm với id = "+productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // không cho thêm quá 5 ảnh đối với 1 sản phẩm
        int size = productImageRepository.findByProductId(productId).size();
        if(size >= ProductImage.MAXIMUM_IMAGE_PER_PRODUCT) {
            throw new InvalidParamException(
                    "số lượng ảnh của sản phẩm phải nhỏ hơn hoặc bằng " +
                            ProductImage.MAXIMUM_IMAGE_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }
}
