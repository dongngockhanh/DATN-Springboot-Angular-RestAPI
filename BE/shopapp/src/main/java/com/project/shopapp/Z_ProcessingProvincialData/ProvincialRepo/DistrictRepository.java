package com.project.shopapp.Z_ProcessingProvincialData.ProvincialRepo;

import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District,Long> {
    List<District> findDistinctByProvinceId(Long id);
}
