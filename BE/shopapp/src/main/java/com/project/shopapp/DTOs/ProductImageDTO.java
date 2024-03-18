package com.project.shopapp.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {

    @JsonProperty("product_id")
    @Min(value = 1,message = "product id phải lớn hơn 0")
    private Long productId;

    @Size(min = 1,max = 200,message = "tên ảnh phải ở khoảng từ 1 đến 200 ký tư")
    @JsonProperty("image_url")
    private String imageUrl;
}
