package com.project.shopapp.configurations;

import com.project.shopapp.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    //user detail object

    @Bean// trả về đối tượng userDetail được xây đựng từ thông tin user nếu tìm thấy user
    public UserDetailsService userDetailsService() {
        return phoneNumber -> userRepository
                    .findByPhoneNumber(phoneNumber)
                    .orElseThrow(()->new UsernameNotFoundException("Can't find users with phone number "+phoneNumber));
    }

    @Bean// mã hoá mật khẩu người dùng SHA-256
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // mã hoá một chiều(one way hashing)
    }

    @Bean// xác thực người dùng
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean//
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
        return config.getAuthenticationManager();
    }
}
