package com.project.shopapp.models;


//import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="users")
@Builder
public class User extends BaseEntity implements UserDetails {
//public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="fullname",length = 100)
    private String fullName;

    @Column(name = "phone_number",length = 15,nullable = false)
    private String phoneNumber;

    @Email(message = "Email không hợp lệ")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String  address;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "facebook_id")
    private Long facebookId;

    @Column(name = "google_id")
    private Long googleId;

    @JoinColumn(name ="role_id")
    @ManyToOne
    private Role roleId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+getRoleId().getName().toUpperCase()));
//        authorityList.add(new SimpleGrantedAuthority("ROLE_user"));
        return authorityList;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
