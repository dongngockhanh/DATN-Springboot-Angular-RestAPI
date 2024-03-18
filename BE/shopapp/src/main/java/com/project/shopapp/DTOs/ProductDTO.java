package com.project.shopapp.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductDTO {
    @NotBlank(message = "Title is requited")
    @Size(min =3,max=200,message = "Title must be between 3 and 200 characters")
    private String name;

    @Min(value=0,message = "price must be greater than or equal to 0")
    @Max(value = 100000000,message = "Price must be less than or equal to 100,000,000")
    private BigDecimal price;

    private String image;
    private String description;

    @JsonProperty("category_id")
    private Long categoryId;

//    private List<MultipartFile> files;
}
