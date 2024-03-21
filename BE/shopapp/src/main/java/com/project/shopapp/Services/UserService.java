package com.project.shopapp.Services;

import com.project.shopapp.DTOs.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.User;
import org.springframework.stereotype.Service;


public interface UserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber,String password) throws Exception;
//    User getUserById(long id);
//    User updateUser(long id,UserDTO userDTO);
//    void deleteUser(long id);
}
