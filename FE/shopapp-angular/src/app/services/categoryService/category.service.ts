import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { CategoryResponse } from '../../responses/categories/category.response';
import { Observable } from 'rxjs';
@Injectable({
    providedIn: 'root'
})
export class CategoryService {
    private apiCategory = `${environment.apiBaseUrl}/categories`;
    constructor(private http: HttpClient) { }
    getCategories(): Observable<CategoryResponse[]> {
        debugger
        return this.http.get<CategoryResponse[]>(this.apiCategory);
    }
    createCategory(name: string): Observable<CategoryResponse> {
        return this.http.post<CategoryResponse>(this.apiCategory, { name });    
    }
    updateCategory(id: number, name: string): Observable<CategoryResponse> {
        return this.http.put<CategoryResponse>(`${this.apiCategory}/${id}`, { name });
    }
    deleteCategory(id: number): Observable<CategoryResponse> {
        return this.http.delete<CategoryResponse>(`${this.apiCategory}/${id}`);
    }
}
