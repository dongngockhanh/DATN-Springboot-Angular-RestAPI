package com.project.shopapp.Z_ProcessingProvincialData.ProvincialController;

import com.project.shopapp.Z_ProcessingProvincialData.ProvincialRepo.CommuneRepository;
import com.project.shopapp.Z_ProcessingProvincialData.ProvincialRepo.DistrictRepository;
import com.project.shopapp.Z_ProcessingProvincialData.ProvincialService.ProvincialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.basePath}/provincial")
@RequiredArgsConstructor
public class ProvincialController {
    private final ProvincialService provincialService;

    @GetMapping("/provinces")
    public ResponseEntity<?> getAllProvince()
    {
        return ResponseEntity.ok(provincialService.getAllProvince());
    }

    @GetMapping("/districts")
    public ResponseEntity<?> getDistrictByProvinceId(@RequestParam("provinceId") Long provinceId) {
        return ResponseEntity.ok(provincialService.getDistrictByProvinceId(provinceId));
    }

    @GetMapping("/communes")
    public ResponseEntity<?> getCommuneByDistrictId(@RequestParam("districtId") Long districtId) {
        return ResponseEntity.ok(provincialService.getCommuneByDistrictId(districtId));
    }
}
