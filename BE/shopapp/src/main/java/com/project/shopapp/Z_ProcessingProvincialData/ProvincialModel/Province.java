package com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "z_provinces")
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private String level;
}
