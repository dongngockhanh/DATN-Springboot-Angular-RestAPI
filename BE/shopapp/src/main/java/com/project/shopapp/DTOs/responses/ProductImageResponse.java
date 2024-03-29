package com.project.shopapp.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.ProductImage;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponse {
    private long id;

    @JsonProperty("product_id")
    private long productId;

    @JsonProperty("image_url")
    private String imageUrl;

}
