package com.project.shopapp.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "số điện thoại là bắt buộc")
    @Size(min = 9,max = 11,message = "số điện thoại không hợp lệ")
    private String phoneNumber;

    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "không được để trống")
    @JsonProperty("retype_password")
    private String retypePassword;

    private String address;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_id")
    private Long facebookId;

    @JsonProperty("google_id")
    private Long googleId;

    @NotNull(message = "Role ID không được để trống")
    @JsonProperty("role_id")
    private Long roleId;
}
