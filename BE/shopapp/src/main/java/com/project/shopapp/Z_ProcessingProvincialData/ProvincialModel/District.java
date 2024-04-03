package com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "z_districts")
public class District {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private String level;

    @ManyToOne
    @JoinColumn(name ="province_id")
    private Province province;

    @Column(name = "name_province")
    private String nameProvince;
}
