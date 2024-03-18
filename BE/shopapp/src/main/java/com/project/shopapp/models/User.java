package com.project.shopapp.models;


//import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionId;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.zip.CheckedOutputStream;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="users")
@Builder
public class User extends BaseEntity{
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
}
