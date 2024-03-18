package com.project.shopapp.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "số điện thoại là bắt buộc")
    private String phoneNumber;

    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
