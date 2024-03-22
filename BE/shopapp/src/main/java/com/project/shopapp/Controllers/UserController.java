package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.UserDTO;
import com.project.shopapp.DTOs.UserLoginDTO;
import com.project.shopapp.Services.UserService;
import com.project.shopapp.exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.basePath}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result)
    {
        try {
            if(result.hasErrors())
            {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword()))
                return ResponseEntity.badRequest().body("mật khẩu nhập lại không khớp");
            userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Đăng ký thành công");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String > login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        // kiểm tra thông tin đăng nhập và sinh ra token
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            return ResponseEntity.ok(token);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
