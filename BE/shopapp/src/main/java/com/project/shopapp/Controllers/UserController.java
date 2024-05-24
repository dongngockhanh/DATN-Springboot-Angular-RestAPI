package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.CommentDTO;
import com.project.shopapp.DTOs.PasswordUpdateDTO;
import com.project.shopapp.DTOs.UserDTO;
import com.project.shopapp.DTOs.UserLoginDTO;
import com.project.shopapp.DTOs.responses.CommentResponse;
import com.project.shopapp.DTOs.responses.LoginResponse;
import com.project.shopapp.DTOs.responses.MessageResponse;
import com.project.shopapp.DTOs.responses.UserResponse;
import com.project.shopapp.Services.UserService;
import com.project.shopapp.exceptions.UnauthorizedException;
import com.project.shopapp.models.User;
import com.project.shopapp.untils.MessageKeys;
import lombok.RequiredArgsConstructor;
import netscape.javascript.JSObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
            String token = userService.createUser(userDTO);
            LoginResponse loginResponse = LoginResponse.builder()
                    .message("đăng ký thành công")
                    .token(token)
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(loginResponse);
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
            LoginResponse loginResponse = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            return ResponseEntity.ok(loginResponse);
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(messageResponse.getMessageResponse(e.getMessage()));
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetail(@PathVariable Long id){
        try{
            User user = userService.getUserDetailById(id);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getListUsers(){
        try {
            List<UserResponse> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    // cập nhật thông tin
    @PutMapping("")
    public ResponseEntity<?> updateUser(
            @Valid @RequestBody UserDTO userDTO,
            @RequestHeader("Authorization") String token,
            BindingResult result){
        try {
            if(result.hasErrors())
            {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            String extractedToken = token.substring(7);
            String tokenReponse = userService.updateUser(extractedToken,userDTO);
            LoginResponse loginResponse = LoginResponse.builder()
                    .message(messageResponse.getMessageString(MessageKeys.UPDATE_USER_SUCCESSFULLY))
                    .token(tokenReponse)
                    .build();
            return ResponseEntity.ok(loginResponse);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageResponse.getMessageResponse(e.getMessage()));
        }
    }
    //chỉ cập nhật mật khẩu
    @PatchMapping("")
    public ResponseEntity<?> updatePassword(
            @RequestHeader ("Authorization") String token,
            @RequestBody PasswordUpdateDTO passwordUpdateDTO){
        try {
            String extractedToken = token.substring(7);
            userService.updatePassword(extractedToken, passwordUpdateDTO);
            return ResponseEntity.ok().body(messageResponse.getMessageResponse(MessageKeys.UPDATE_USER_SUCCESSFULLY));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(messageResponse.getMessageResponse(e.getMessage()));
        }
    }
    @DeleteMapping("/{id}")//xoá mềm
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            userService.setActiveUser(id);
            return ResponseEntity.ok().body(messageResponse.getMessageResponse(MessageKeys.DELETE_USER_SUCCESSFULLY));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(messageResponse);
        }
    }
    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO){
        try {
            List<CommentResponse> comment = userService.createComment(commentDTO);
            return ResponseEntity.ok().body(comment);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/comment")
    public ResponseEntity<?> getComment(@RequestParam("productId") long id){
        List<CommentResponse> commentResponses = userService.getComments(id);
        return ResponseEntity.ok().body(commentResponses);
    }
    @GetMapping("/otp/resend")
    public void resendOtp(@RequestParam("phoneNumber") String phoneNumber){
        userService.resendOtp(phoneNumber);
    }
    @GetMapping("/otp/verify")
    public boolean verifyOtp(@RequestParam("otp") String otp){
        return userService.verifyTwoFa(otp);
    }

    @PatchMapping("/{id}/changeTwoFa")
    public void changeTwoFa(@PathVariable("id") Long id){
        userService.changeTwoFa(id);
    }
}
