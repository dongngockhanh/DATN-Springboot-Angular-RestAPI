package com.project.shopapp.Services;

import com.project.shopapp.DTOs.PasswordUpdateDTO;
import com.project.shopapp.DTOs.UserDTO;
import com.project.shopapp.DTOs.responses.UserResponse;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber,String password) throws Exception;
    User getUserDetailById(Long userId) throws Exception;
    List<UserResponse> getAllUsers();
//    User getUserById(long id);
    String updateUser(String token,UserDTO userDTO) throws Exception;
    void updatePassword(String token, PasswordUpdateDTO passwordUpdateDTO) throws Exception;
    void setActiveUser(Long userId) throws Exception;//xoá mềm
}
