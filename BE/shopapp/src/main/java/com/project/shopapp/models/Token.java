package com.project.shopapp.models;


//import jakarta.persistence.*;
import lombok.*;
import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="tokens")
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token",nullable = false,length = 255)
    private String token;

    @Column(name = "token_type",nullable = false,length = 100)
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;//ngày hết hạn token

    @Column(name ="revoked",nullable = false)// bị huỷ chưa
    private int revoked;

    @Column(name ="expired", nullable = false)// hết hạn chưa
    private int expired;

    @ManyToOne
    @JoinColumn(name= "user_id")
    private User user;

}
