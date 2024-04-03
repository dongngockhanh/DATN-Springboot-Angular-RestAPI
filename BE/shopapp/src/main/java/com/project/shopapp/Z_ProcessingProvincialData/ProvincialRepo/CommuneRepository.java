package com.project.shopapp.Z_ProcessingProvincialData.ProvincialRepo;

import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommuneRepository extends JpaRepository<Commune,Long> {
    List<Commune> findCommuneByDistrictId(Long id);
}
