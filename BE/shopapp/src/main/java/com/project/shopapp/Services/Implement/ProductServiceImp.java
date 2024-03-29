package com.project.shopapp.Services.Implement;

import com.project.shopapp.DTOs.ProductDTO;
import com.project.shopapp.DTOs.ProductImageDTO;
import com.project.shopapp.DTOs.responses.MessageResponse;
import com.project.shopapp.DTOs.responses.ProductImageResponse;
import com.project.shopapp.DTOs.responses.ProductResponse;
import com.project.shopapp.Repositories.CategoryRepository;
import com.project.shopapp.Repositories.ProductImageRepository;
import com.project.shopapp.Repositories.ProductRepository;
import com.project.shopapp.Services.ProductService;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.untils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final MessageResponse messageResponse;
    private final ModelMapper modelMapper;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new
                        DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_CATEGORY,productDTO.getCategoryId())));
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
    public Page<ProductResponse> getAllProducts(String keyword,Long categoryId,PageRequest pageRequest) {
        // lấy danh sách sản phẩm theo page(trang) và limit(giới hạn sản phẩm trong trang) và categoryId nếu có
        Page<Product> productPage;
        productPage = productRepository.searchProducts(categoryId.intValue(),keyword,pageRequest);
//        productPage = productRepository.findAll(pageRequest);

        return productPage.map(ProductResponse::fromProduct);
//       return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product getProductById(long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_PRODUCT,id)));
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
       Product existingProduct = getProductById(id);
       if(existingProduct != null) {
           //copy từ dto sang entity
           //sử dụng model mapper
           Category existingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                   .orElseThrow(()-> new
                           DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_CATEGORY,productDTO.getCategoryId())));
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
                new DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_PRODUCT,productImageDTO.getProductId())));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // không cho thêm quá 5 ảnh đối với 1 sản phẩm
        int size = productImageRepository.findByProductId(productId).size();
        if(size >= ProductImage.MAXIMUM_IMAGE_PER_PRODUCT) {
            throw new InvalidParamException(messageResponse.getMessageString(MessageKeys.NO_MORE_FIVE_IMAGE_FOR_PRODUCT,
                            ProductImage.MAXIMUM_IMAGE_PER_PRODUCT));
        }
        return productImageRepository.save(newProductImage);
    }

    @Override
    public List<ProductImageResponse> getImageByProductId(long id)
    {
        List<ProductImage> productImageList = productImageRepository.findByProductId(id);
        Type listType = new TypeToken<List<ProductImageResponse>>() {}.getType();
        return modelMapper.map(productImageList,listType);
    }
}
