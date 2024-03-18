package com.project.shopapp.DTOs;

import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data //tostring
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @NotEmpty(message = "Category name cannot be empty")
    private String name;
}
