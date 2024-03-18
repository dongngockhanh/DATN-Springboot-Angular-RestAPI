package com.project.shopapp.Services.Implement;

import com.project.shopapp.DTOs.UserDTO;
import com.project.shopapp.Repositories.RoleRepository;
import com.project.shopapp.Repositories.UserRepository;
import com.project.shopapp.Services.UserService;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
       String phoneNumber = userDTO.getPhoneNumber();
       if(userRepository.existsByPhoneNumber(phoneNumber))
           throw new DataIntegrityViolationException("số điện thoại đã được đăng ký");
       User newUser = User.builder()
               .fullName(userDTO.getFullName())
               .phoneNumber(userDTO.getPhoneNumber())
               .email(userDTO.getEmail())
               .password(userDTO.getPassword())
               .address(userDTO.getAddress())
               .dateOfBirth(userDTO.getDateOfBirth())
               .facebookId(userDTO.getFacebookId())
               .googleId(userDTO.getGoogleId())
               .build();
       Role role = roleRepository.findById(userDTO.getRoleId())
               .orElseThrow(()->new DataNotFoundException("không tìm thấy role"));
       newUser.setRoleId(role);
       // kiểm tra nếu có các social account id thì không yêu cầu password
        if(userDTO.getFacebookId()==0||userDTO.getGoogleId()==0)
        {
            String password = userDTO.getPassword();

            // phần dưới này để làm tronng phần security
//            String encodedPassword = passwordEncoder.encode(password); // mã hoá mật khẩu
//            newUser.setPassword(encodedPassword);
        }
       return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        // trong phần security
        return null;
    }
}
