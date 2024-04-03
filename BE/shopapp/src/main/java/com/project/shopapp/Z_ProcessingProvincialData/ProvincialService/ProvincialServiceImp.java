package com.project.shopapp.Z_ProcessingProvincialData.ProvincialService;

import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.Commune;
import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.District;
import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.Province;
import com.project.shopapp.Z_ProcessingProvincialData.ProvincialRepo.CommuneRepository;
import com.project.shopapp.Z_ProcessingProvincialData.ProvincialRepo.DistrictRepository;
import com.project.shopapp.Z_ProcessingProvincialData.ProvincialRepo.ProvinceRepository;
import com.project.shopapp.exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvincialServiceImp implements ProvincialService{
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final CommuneRepository communeRepository;

    @Override
    public List<Province> getAllProvince() {
        return provinceRepository.findAll();
    }

    @Override
    public List<District> getDistrictByProvinceId(Long ProvinceId) {
        provinceRepository.findById(ProvinceId).orElseThrow(()->new DataNotFoundException("không tìm thấy tỉnh"));
        return districtRepository.findDistinctByProvinceId(ProvinceId);
    }

    @Override
    public List<Commune> getCommuneByDistrictId(Long DistrictId) {
        districtRepository.findById(DistrictId).orElseThrow(()->new DataNotFoundException("không tìm thấy huyện"));
        return communeRepository.findCommuneByDistrictId(DistrictId);
    }
}
