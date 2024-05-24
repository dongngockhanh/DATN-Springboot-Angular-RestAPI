package com.project.shopapp.Services;

import com.project.shopapp.DTOs.CommentDTO;
import com.project.shopapp.DTOs.PasswordUpdateDTO;
import com.project.shopapp.DTOs.UserDTO;
import com.project.shopapp.DTOs.responses.CommentResponse;
import com.project.shopapp.DTOs.responses.LoginResponse;
import com.project.shopapp.DTOs.responses.UserResponse;
import com.project.shopapp.models.User;

import java.util.List;


public interface UserService {
    String createUser(UserDTO userDTO) throws Exception;
    LoginResponse login(String phoneNumber, String password) throws Exception;
    User getUserDetailById(Long userId) throws Exception;
    List<UserResponse> getAllUsers();
//    User getUserById(long id);
    String updateUser(String token,UserDTO userDTO) throws Exception;
    void updatePassword(String token, PasswordUpdateDTO passwordUpdateDTO) throws Exception;
    void setActiveUser(Long userId) throws Exception;//xoá mềm
    List<CommentResponse> createComment(CommentDTO commentDTO);
    List<CommentResponse> getComments(long productid);
//    void deleteComment(Long commentId) throws Exception;
    void resendOtp(String phoneNumber);
    boolean verifyTwoFa(String otp);
    void changeTwoFa(long id);
//    void inputOtp(String otpInput);
}
