package com.project.shopapp.Repositories;

import com.project.shopapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByPhoneNumber(String phoneNumber); //kiểm tra tài khoản user theo sđt phục vụ login
    boolean existsByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);// <=> SELECT * FROM users WHERE phoneNumber=?
    Optional<User> findByEmail(String email);
}
