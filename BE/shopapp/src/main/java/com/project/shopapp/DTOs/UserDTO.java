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
    @NotBlank(message = "Phone number is required")
    @Size(min = 9,max = 11,message = "invalid phone number")
    private String phoneNumber;

    private String email;

    @NotBlank(message = "Password can not be blank")
    private String password;

    @NotBlank(message = "cannot be left blank")
    @JsonProperty("retype_password")
    private String retypePassword;

    private String address;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_id")
    private Long facebookId;

    @JsonProperty("google_id")
    private Long googleId;

    @NotNull(message = "Role ID cannot be empty")
    @JsonProperty("role_id")
    private Long roleId;
}
