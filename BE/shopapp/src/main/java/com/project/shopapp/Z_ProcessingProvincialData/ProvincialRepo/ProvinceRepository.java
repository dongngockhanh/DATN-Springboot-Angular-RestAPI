package com.project.shopapp.Z_ProcessingProvincialData.ProvincialRepo;

import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Long> {
}
