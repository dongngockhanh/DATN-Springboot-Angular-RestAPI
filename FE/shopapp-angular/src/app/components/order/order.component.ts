import { Component } from '@angular/core';
import { ProvincialService } from '../../services/provincial.service';
import { ProvincialResponse } from '../../responses/provincial/provincial.response';
import { Province } from '../../responses/provincial/province';
import { District } from '../../responses/provincial/district';
import { Ward } from '../../responses/provincial/ward';
import { Form, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent {

  // lấy danh sách các tỉnh thành
  provinceId: string = '';
  districtId: string = '';
  formGroup!: FormGroup;
  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  filteredProvinces: Province[] = [];
  filteredDistricts: District[] = [];
  filteredWards: Ward[] = [];
  constructor(private provincialService: ProvincialService, private fb: FormBuilder) {
  }
  ngOnInit() {
    this.initForm();
    this.getProvinces();
  }
  initForm() {
    this.formGroup = this.fb.group({
      'employee': [''],
      'district': [''],
      'ward': ['']
    });
    this.formGroup.get('employee')?.valueChanges.subscribe((enteredData: string) => {
      this.fillerDataProvince(enteredData);
    });
    this.formGroup.get('district')?.valueChanges.subscribe((enteredData: string) => {
      this.fillerDataDistrict(enteredData);
    });
    this.formGroup.get('ward')?.valueChanges.subscribe((enteredData: string) => {
      this.fillterDataWard(enteredData);
    });
  }
  fillerDataProvince(enteredData: string){
    const filteredProvinces = this.provinces.filter(province => {
      return province.province_name.toLowerCase().includes(enteredData.toLowerCase());
    });
    if (filteredProvinces.length === 0) {
      this.filteredProvinces = [];
      this.filteredProvinces.push({province_id: '', province_name: 'Không tìm thấy tỉnh'});
      console.log(filteredProvinces);
    }else{
      debugger
      this.filteredProvinces = filteredProvinces;
      const matchedProvince = this.provinces.find(province => province.province_name === enteredData);
      if (matchedProvince) {
        console.log('Tìm thấy tỉnh:', matchedProvince);
        this.provinceId = matchedProvince.province_id;
        console.log('ID tỉnh:', this.provinceId);
        this.filteredDistricts = [];
        this.getDistricts();
      } 
    }
  }
  fillerDataDistrict(enteredData: string){
    const filteredDistricts = this.districts.filter(district => {
      return district.district_name.toLowerCase().includes(enteredData.toLowerCase());
    });
    if (filteredDistricts.length === 0) {
      this.filteredDistricts = [];
      this.filteredDistricts.push({district_id: '', district_name: 'Không tìm thấy huyện'});
      console.log(filteredDistricts);
    }else{
      debugger
      this.filteredDistricts = filteredDistricts;
      const matchedDistrict = this.districts.find(district => district.district_name === enteredData);
      if (matchedDistrict) {
        console.log('Tìm thấy huyện:', matchedDistrict);
        this.districtId = matchedDistrict.district_id;
        console.log('ID huyện:', this.districtId);
        this.filteredWards = [];
        this.getWards();
      } 
    }
  }
  fillterDataWard(enteredData: string){
    const filteredWards = this.wards.filter(ward => {
      return ward.ward_name.toLowerCase().includes(enteredData.toLowerCase());
    });
    if (filteredWards.length === 0) {
      this.filteredWards = [];
      this.filteredWards.push({ward_id: '', ward_name: 'Không tìm thấy phường'});
      console.log(filteredWards);
    }else{
      debugger
      this.filteredWards = filteredWards;
      const matchedWard = this.wards.find(ward => ward.ward_name === enteredData);
      if (matchedWard) {
        console.log('Tìm thấy phường:', matchedWard);
        // this.wardId = Number(matchedWard.ward_id);
        // console.log('ID phường:', this.wardId);
      } 
    }
  }


  getProvinces() {
    this.provincialService.getProvinces().subscribe({
      next: (response : any)=> {
        this.provinces = response.results;
        this.filteredProvinces = response.results;
      },
      complete: () => {
        debugger
        console.log('Complete');
      },
      error: (error) => {
        debugger
        console.log(error);
      }
    });
  }
  getDistricts() {
    this.provincialService.getDistricts(this.provinceId).subscribe({
      next: (response : any)=> {
        debugger
        this.districts = response.results;
        this.filteredDistricts = response.results;
        console.log(response);
      },
      complete: () => {
        debugger
        console.log('Complete');
      },
      error: (error) => {
        debugger
        console.log(error);
      }
    });
  }
  getWards() {
    this.provincialService.getWards(this.districtId).subscribe({
      next: (response : any)=> {
        debugger
        this.wards = response.results;
        this.filteredWards = response.results;
        console.log(response);
      },
      complete: () => {
        debugger
        console.log('Complete');
      },
      error: (error) => {
        debugger
        console.log(error);
      }
    })
  }
  // đến đây là xong phần lấy dữ liệu từ API
}
