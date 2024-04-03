package com.project.shopapp.Z_ProcessingProvincialData.ProvincialService;

import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.Commune;
import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.District;
import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.Province;

import java.util.List;

public interface ProvincialService {
    List<Province> getAllProvince();
    List<District> getDistrictByProvinceId(Long ProvinceId);
    List<Commune> getCommuneByDistrictId(Long DistrictId);
}
