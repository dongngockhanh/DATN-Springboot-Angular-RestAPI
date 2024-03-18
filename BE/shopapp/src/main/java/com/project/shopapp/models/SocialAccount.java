package com.project.shopapp.models;

//import jakarta.persistence.*;
import lombok.*;
import javax.persistence.*;


import java.util.concurrent.CountDownLatch;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="social_accounts")
@Builder
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider",nullable = false,length = 100)
    private String provider;

    @Column(name = "provider_id",nullable = false,length = 100)
    private String providerId;

    @Column(name = "email",nullable = false,length = 100)
    private String email;

    @Column(name = "name",nullable = false,length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
