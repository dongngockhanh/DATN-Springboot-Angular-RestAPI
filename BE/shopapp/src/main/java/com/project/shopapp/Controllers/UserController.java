package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.UserDTO;
import com.project.shopapp.DTOs.UserLoginDTO;
import com.project.shopapp.DTOs.responses.MessageResponse;
import com.project.shopapp.DTOs.responses.UserResponse;
import com.project.shopapp.Services.UserService;
import com.project.shopapp.exceptions.UnauthorizedException;
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
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result, HttpServletRequest request)
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
                return ResponseEntity.badRequest().body("retype password does not match");
            userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Register successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
//            Locale locale = localeResolver.resolveLocale(request);
            UserResponse userResponse = UserResponse.builder()
                    .message(messageResponse.getMessageResponse("user.login.login_successfully"))
                    .token(token)
                    .build();
            return ResponseEntity.ok(userResponse);
        }catch (UnauthorizedException e){
            messageResponse.getMessageResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageResponse);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
