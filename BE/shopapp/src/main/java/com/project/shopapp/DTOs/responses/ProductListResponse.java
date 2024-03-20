package com.project.shopapp.DTOs.responses;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
    // tuỳ biến response
    private List<ProductResponse> products;
    private int totalPages;
}
