package com.project.shopapp.Services.Implement;

import com.project.shopapp.DTOs.CommentDTO;
import com.project.shopapp.DTOs.PasswordUpdateDTO;
import com.project.shopapp.DTOs.UserDTO;
import com.project.shopapp.DTOs.responses.CommentResponse;
import com.project.shopapp.DTOs.responses.LoginResponse;
import com.project.shopapp.DTOs.responses.MessageResponse;
import com.project.shopapp.DTOs.responses.UserResponse;
import com.project.shopapp.Repositories.CommentRepository;
import com.project.shopapp.Repositories.ProductRepository;
import com.project.shopapp.Repositories.RoleRepository;
import com.project.shopapp.Repositories.UserRepository;
import com.project.shopapp.Services.UserService;
import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.PermissionDenyException;
import com.project.shopapp.exceptions.UnauthorizedException;
import com.project.shopapp.models.Comment;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.untils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final MessageResponse messageResponse;
    @Override
    public String createUser(UserDTO userDTO) throws Exception {
       String phoneNumber = userDTO.getPhoneNumber();
       if(userRepository.existsByPhoneNumber(phoneNumber))
           throw new DataIntegrityViolationException(MessageKeys.REGISTERED_NUMBER_PHONE);
       Role role = roleRepository.findById(userDTO.getRoleId())
               .orElseThrow(()->new DataNotFoundException(MessageKeys.ROLE_NOT_FOUND));
       if(role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException(MessageKeys.NOT_REGISTER_ADMIN_ACCOUNT);
       }
       User newUser = User.builder()
               .fullName(userDTO.getFullName())
               .phoneNumber(userDTO.getPhoneNumber())
               .email(userDTO.getEmail())
               .password(userDTO.getPassword())
               .address(userDTO.getAddress())
               .isActive(true)
               .dateOfBirth(userDTO.getDateOfBirth())
               .facebookId(userDTO.getFacebookId())
               .googleId(userDTO.getGoogleId())
               .build();
       newUser.setRoleId(role);
       // kiểm tra nếu có các social account id thì không yêu cầu password
        if(userDTO.getFacebookId() == 0 && userDTO.getGoogleId() == 0)
        {
            // sử dụng spring security
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password); // mã hoá mật khẩu
            newUser.setPassword(encodedPassword);
        }
        userRepository.save(newUser);
        return jwtTokenUtil.generateToken(newUser);
    }

    @Override
    public LoginResponse login(String phoneNumber, String password) throws Exception{
        // sử dụng spring security
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(!optionalUser.isPresent()) {
            throw new UnauthorizedException(MessageKeys.LOGIN_FAILED);
        //  throw new DataNotFoundException("phone number hoặc password không hợp lệ");
        }
        User existingUser = optionalUser.get();
        // kiểm tra active user
        if(!existingUser.isActive()){
            throw new UnauthorizedException(MessageKeys.LOGIN_FAILED);
        }
        // check password
        if(existingUser.getFacebookId() == 0 && existingUser.getGoogleId() == 0) {
            if(!passwordEncoder.matches(password,existingUser.getPassword()))
        //      throw new BadCredentialsException("sai số điện thoại hoặc password");
                throw new UnauthorizedException(MessageKeys.LOGIN_FAILED);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber,password, existingUser.getAuthorities()
        );
        // authentication với java spring security
        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenUtil.generateToken(existingUser);
        if(existingUser.isTwoFa()){
                sendOtpToMail(existingUser);
        }
        return LoginResponse.builder()
                .message("thành công")
                .token(token)
                .twoFa(existingUser.isTwoFa())
                .build();
    }

    @Override
    public User getUserDetailById(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(()->new DataNotFoundException(MessageKeys.NOT_FOUND_USER_BY_ID));
        return user;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for(User user :users){
            if(user.getRoleId().getId()==1){
                userResponses.add(UserResponse.fromUser(user));
            }
        }

        return userResponses;
    }

    @Override
    public String updateUser(String token, UserDTO userDTO) throws Exception {
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(()->new DataNotFoundException("Could not find"));
        Optional<User> existingUser = userRepository.findByPhoneNumber(userDTO.getPhoneNumber());
        if(existingUser.isPresent()&&!existingUser.get().equals(user)){
            throw new DataIntegrityViolationException(MessageKeys.REGISTERED_NUMBER_PHONE);
        }else{
            user.setFullName(userDTO.getFullName());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setEmail(userDTO.getEmail());
            if(userDTO.getPassword()!=null){
                String password = userDTO.getPassword();
                String encodedPassword = passwordEncoder.encode(password); // mã hoá mật khẩu
                user.setPassword(encodedPassword);
            }
            user.setAddress(userDTO.getAddress());
            user.setActive(true);
            user.setDateOfBirth(userDTO.getDateOfBirth());
            user.setFacebookId(userDTO.getFacebookId());
            user.setGoogleId(userDTO.getGoogleId());
            userRepository.save(user);
            return jwtTokenUtil.generateToken(user);
        }
    }

    @Override
    public void updatePassword(String token, PasswordUpdateDTO passwordUpdateDTO) throws Exception {
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()->new DataNotFoundException("Could not find"));
        if(!passwordEncoder.matches(passwordUpdateDTO.getOldPassword(),user.getPassword())){
            throw new UnauthorizedException(MessageKeys.OLD_PASSWORD_IS_INCORRECT);
        }
        String newPassword = passwordUpdateDTO.getNewPassword();
        String encodedPassword = passwordEncoder.encode(newPassword); // mã hoá mật khẩu
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override // xoá mềm cập nhật trạng thái active
    public void setActiveUser(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_USER_BY_ID,userId)));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    @Override
    public List<CommentResponse> createComment(CommentDTO commentDTO) {
        User existingUser = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(()->new DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_USER_BY_ID,commentDTO.getUserId())));
        Product existingProduct = productRepository.findById(commentDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException(messageResponse.getMessageString((MessageKeys.NOT_FOUND_PRODUCT))));
        Comment comment = Comment.builder()
                .content(commentDTO.getContent())
                .user(existingUser)
                .product(existingProduct)
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment);

        return getComments(comment.getProduct().getId());
    }

    @Override
    public List<CommentResponse> getComments(long productid) {
        Product existingProduct = productRepository.findById(productid)
                .orElseThrow(()->new DataNotFoundException(messageResponse.getMessageString((MessageKeys.NOT_FOUND_PRODUCT))));
        List<Comment> comments = commentRepository.findCommentByProductId(productid);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for(Comment commentResponse:comments)
        {
            commentResponses.add(CommentResponse.fromComment(commentResponse));
        }
        commentResponses.sort((i1,i2)->i2.getCreatedAt().compareTo(i1.getCreatedAt()));
        return commentResponses;
    }


    private final JavaMailSender javaMailSender;
    public static String OTP;
    @Async
    public void sendOtpToMail(User user) {
        try{
            String mailOfUser = user.getEmail();
            String otp = String.valueOf(new Random().nextInt(8999) + 1000);
            SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("cuccut@gmail.com");
            message.setTo(mailOfUser);
            message.setSubject("Xác thực OTP");
            message.setText("mã xác thực của bạn là: "+otp);
            OTP = otp;
            javaMailSender.send(message);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void resendOtp(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
               .orElseThrow(()->new DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_USER_BY_ID,phoneNumber)));
        sendOtpToMail(user);
    }

    @Override
    public boolean verifyTwoFa(String otp) {
        return otp.equals(OTP);
    }

    @Override
    public void changeTwoFa(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_USER_BY_ID,id)));
        user.setTwoFa(!user.isTwoFa());
        userRepository.save(user);
    }

}
