package com.project.shopapp.models;

//import jakarta.persistence.*;
import lombok.*;
import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="roles")
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,name ="name",length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    public static String ADMIN = "ADMIN";
    public static String USER = "USER";
}
