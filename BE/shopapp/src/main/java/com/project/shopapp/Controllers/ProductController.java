package com.project.shopapp.Controllers;

import com.github.javafaker.Faker;
import com.project.shopapp.DTOs.ProductDTO;
import com.project.shopapp.DTOs.ProductImageDTO;
import com.project.shopapp.DTOs.responses.MessageResponse;
import com.project.shopapp.DTOs.responses.ProductListResponse;
import com.project.shopapp.DTOs.responses.ProductResponse;
import com.project.shopapp.Services.ProductService;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.untils.MessageKeys;
import com.sun.jndi.toolkit.url.Uri;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
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

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.MarshalException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
//@Validated
@RequestMapping("${api.basePath}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final MessageResponse messageResponse;

    @GetMapping("")// http://localhost:8888/api/v1/products?page=1&limit=10
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name="category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit
    ) {
        //taọ Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(page, limit
//                , Sort.by("createdAt").descending()
                , Sort.by("id").ascending()
        );
        Page<ProductResponse> productPage = productService.getAllProducts(keyword,categoryId,pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId)
    {
        try {
            Product existingProduct = productService.getProductById(productId);
            ProductResponse response = ProductResponse.fromProduct(existingProduct);
            response.setProductImages(productService.getImageByProductId(productId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(messageResponse);
        }
    }

    @GetMapping("/by-ids")// phục vụ lấy danh sách giỏ hàng
    public ResponseEntity<?> getProductByIds(@RequestParam("ids") String ids)
    {
        List<Long> productIds = Arrays.stream(ids.split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
        List<Product> products = productService.getProductByIds(productIds);
        return ResponseEntity.ok(products);
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
            return ResponseEntity.badRequest().body(messageResponse);
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
                return ResponseEntity.badRequest().body(messageResponse.getMessageResponse(MessageKeys.ONLY_UPLOAD_FIVE_IMAGE));
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
                                .body(messageResponse.getMessageResponse(MessageKeys.SIZE_IS_LESS_BE_THAN_TEN));
                    }
                    String contentType = file.getContentType();
                    if(contentType==null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .body(messageResponse.getMessageResponse(MessageKeys.FILE_MUST_BE_IMAGE));
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
            return ResponseEntity.badRequest().body(messageResponse);
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

    @GetMapping("images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName){
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if(resource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            }else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id,@RequestBody ProductDTO productDTO)
    {
        try {
            Product updateProduct = productService.updateProduct(id,productDTO);
            return ResponseEntity.ok(messageResponse.getMessageResponse(MessageKeys.UPDATE_PRODUCT_SUCCESSFULLY,id));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(messageResponse);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducts(@PathVariable Long id)
    {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(messageResponse.getMessageResponse(MessageKeys.DELETE_PRODUCT_SUCCESSFULLY,id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(messageResponse);
        }
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
