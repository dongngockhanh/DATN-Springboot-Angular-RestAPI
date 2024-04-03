import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http"; // Add missing import
import { Province } from "../responses/provincial/province";
import { environment } from "../environments/environment";
import { ProvincialResponse } from "../responses/provincial/provincial.response";
import { District } from "../responses/provincial/district";
import { Ward } from "../responses/provincial/ward";
import { map } from 'rxjs/operators';
@Injectable({
    providedIn: 'root'
})
export class ProvincialService {  
    // environment https://cors-anywhere.herokuapp.com ctrl + click vào link để active api nếu hêt hạn
    private corsAnywhere = 'https://cors-anywhere.herokuapp.com';
    
    // private apiProvinceUrl = `${environment.apiBaseUrl}/provincial/provinces`// Add API URL
    private apiProvinceUrl = `${this.corsAnywhere}/https://vapi.vnappmob.com/api/province`; // Add API URL
    private apiDistrictUrl = `${this.corsAnywhere}/https://vapi.vnappmob.com/api/province/district`; // Add API URL
    private apiWardUrl = `${this.corsAnywhere}/https://vapi.vnappmob.com/api/province/ward`; // Add API URL

    constructor(private http: HttpClient) {} // Add constructor with HttpClient injection
    getProvinces(): Observable<ApiResponseProvince> {
        return this.http.get<ApiResponseProvince>(this.apiProvinceUrl)
    }

    getDistricts(provinceId: string): Observable<ApiResponseDistrict> {
        return this.http.get<ApiResponseDistrict>(`${this.apiDistrictUrl}/${provinceId}`); // Add type annotation to get method call
    }

    getWards(districtId: string): Observable<ApiResponseWard> {
        return this.http.get<ApiResponseWard>(`${this.apiWardUrl}/${districtId}`); // Add type annotation to get method call
    }
}
interface ApiResponseProvince {
    results: Province[];
}
interface ApiResponseDistrict {
    results: District[];
}
interface ApiResponseWard {
    results: Ward[];
}