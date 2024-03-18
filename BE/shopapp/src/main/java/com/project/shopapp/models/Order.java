package com.project.shopapp.models;

//import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="orders")
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="fullname",length = 100)
    private String fullName;

    @Column(name = "email",length = 100)
    private String email;

    @Column(name = "phone_number",length = 15,nullable = false)
    private String phoneNumber;

    @Column(name = "address",length = 255,nullable = false)
    private String  address;

    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_money")
    private BigDecimal totalMoney;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    private String status;

    @Column(name = "shipping_method",length = 100)
    private String shippingMethod;

    @Column(name = "payment_method",length = 100)
    private String paymentMethod;

    @Column(name = "shipping_date")
    private Date shippingDate;

    @Column(name = "tracking_number",length = 100)
    private String trackingNumber;

    @Column(name = "shipping_address",length = 100)
    private String shippingAddress;

    @Column(name = "active")
    private boolean active;
}
