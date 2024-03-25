package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.UserDTO;
import com.project.shopapp.DTOs.UserLoginDTO;
import com.project.shopapp.DTOs.responses.MessageResponse;
import com.project.shopapp.DTOs.responses.UserResponse;
import com.project.shopapp.Services.UserService;
import com.project.shopapp.exceptions.UnauthorizedException;
import com.project.shopapp.untils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.basePath}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
//    private final MessageSource messageSource;
//    private final LocaleResolver localeResolver;
    private final MessageResponse messageResponse;

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
                return ResponseEntity.badRequest()
                        .body(messageResponse.getMessageResponse(MessageKeys.PASSWORD_NOT_MATCH));
            userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(messageResponse.getMessageResponse(MessageKeys.REGISTER_SUCCESSFULLY));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageResponse.getMessageResponse(e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletRequest request
    ){
        // kiểm tra thông tin đăng nhập và sinh ra token
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            UserResponse userResponse = UserResponse.builder()
                    .message(messageResponse.getMessageString(MessageKeys.LOGIN_SUCCESSFULLY))
                    .token(token)
                    .build();
            return ResponseEntity.ok(userResponse);
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(messageResponse.getMessageResponse(e.getMessage()));
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
