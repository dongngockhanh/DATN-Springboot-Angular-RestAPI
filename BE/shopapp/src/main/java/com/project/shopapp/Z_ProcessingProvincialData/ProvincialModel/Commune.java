package com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "z_communes")
public class Commune {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private String level;


    @ManyToOne
    @JoinColumn(name ="district_id")
    private District district;

    @Column(name = "name_district")
    private String nameDistrict;

    @ManyToOne
    @JoinColumn(name ="province_id")
    private Province province;

    @Column(name = "name_province")
    private String nameProvince;
}
