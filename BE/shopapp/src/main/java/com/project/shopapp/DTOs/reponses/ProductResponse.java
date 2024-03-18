package com.project.shopapp.DTOs.reponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.DTOs.ProductDTO;
import com.project.shopapp.models.Product;
import lombok.*;
import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse extends BaseResponse{
    private String name;
    private BigDecimal price;
    private String image;
    private String description;

    @JsonProperty("category_id")
    private Long categoryId;

//    @JsonProperty("name")
//    private String categoryName;

    public static ProductResponse fromProduct(Product product)
    {
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .image(product.getImage())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
