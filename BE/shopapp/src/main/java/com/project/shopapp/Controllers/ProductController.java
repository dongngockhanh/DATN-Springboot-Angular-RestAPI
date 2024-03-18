package com.project.shopapp.Controllers;

import com.github.javafaker.Faker;
import com.project.shopapp.DTOs.ProductDTO;
import com.project.shopapp.DTOs.ProductImageDTO;
import com.project.shopapp.DTOs.reponses.ProductListResponse;
import com.project.shopapp.DTOs.reponses.ProductResponse;
import com.project.shopapp.Services.ProductService;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
//@Validated
@RequestMapping("${api.basePath}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("")// http://localhost:8888/api/v1/products?page=1&limit=10
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        //taọ Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable("id") String productId)
    {
        return ResponseEntity.ok("Product with ID: "+productId);
    }

    @PostMapping("")
    public ResponseEntity<?>createProducts(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try{
            if(result.hasErrors())
            {
                List<String> errorMessages =  result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);

            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId ,@ModelAttribute("files")  List<MultipartFile> files)
    {
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files ==null ? new ArrayList<MultipartFile>(): files;
            if(files.size()>ProductImage.MAXIMUM_IMAGE_PER_PRODUCT)
            {
                return ResponseEntity.badRequest().body("bạn chỉ có thể upload 5 ảnh");
            }
            List<ProductImage> productImageList = new ArrayList<>();
            for(MultipartFile file : files)
            {
                if(file.getSize()==0) {
                    continue;
                }
                    //kiểm tra kích thước định dạng file ảnh
                    if(file.getSize()>10*1024*1024) {
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .body(" <10MB");
                    }
                    String contentType = file.getContentType();
                    if(contentType==null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .body("file must be is image");
                    }
                    String filename = storeFile(file);
                    // lưu vào đối tượng product trong DB
                        ProductImage productImage = productService.createProductImage(
                            existingProduct.getId(),
                            ProductImageDTO.builder()
                                    .imageUrl(filename)
                                    .build()
                    );
                    productImageList.add(productImage);
            }
            return ResponseEntity.ok(productImageList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException{
        if(!isImageFile(file) || file.getOriginalFilename()==null)
        {
            throw new IOException("Invalid image file format");
        }

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString()+"_"+filename;
        //đường dẫn đến thư mục muốn lưu file
        Path uploadDir = Paths.get("uploads");
        //kiểm tra và tạo thư mục nếu không tồn tại
        if(!Files.exists(uploadDir))
        {
            Files.createDirectories(uploadDir);
        }
        // đường dẫn đầy đủ đén file
        Path destination = Paths.get(uploadDir.toString(),uniqueFilename);
        //sao chép file vào thư mục đích
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file)
    {
        String contentType =file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateProducts(@PathVariable long id)
    {
        return ResponseEntity.ok("update product with id = "+id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProducts(@PathVariable Long id)
    {
        return ResponseEntity.status(HttpStatus.OK).body("Product delete successfully");
    }

    // fake products data
//    @PostMapping("/generateFakeProducts") public
    private ResponseEntity<String> generateFakeProduct(){
        Faker faker = new Faker();
        for(int i=0;i<1000;i++)
        {
            String productName = faker.commerce().productName();
            if(productService.existProductByName(productName))
            {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price(BigDecimal.valueOf(faker.number().numberBetween(10,90000000)))
                    .image("")
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(1,5))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("fake products tạo thành công");
    }
}
