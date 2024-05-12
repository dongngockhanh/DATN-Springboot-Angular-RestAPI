import { Injectable, numberAttribute } from "@angular/core";
import { HttpClient, HttpErrorResponse, HttpEvent, HttpHeaders, HttpParams, HttpRequest } from "@angular/common/http";
import { ProductResponse } from "../../responses/products/product.response";
import { environment } from "../../environments/environment";
import { catchError, Observable, throwError } from "rxjs";
@Injectable({
    providedIn: 'root'
})
export class ProductService{
    private apiProduct = `${environment.apiBaseUrl}/products`;
    constructor(private http: HttpClient) { }

    getProducts(keyword:string, categoryid:number, page : number,limit : number): Observable<ProductResponse[]>{
        categoryid = categoryid !== undefined ? categoryid : 0;
        const params = new HttpParams()
        .set('keyword', keyword)
        .set('category_id', categoryid.toString())
        .set('page', page.toString())
        .set('limit', limit.toString());
        return this.http.get<ProductResponse[]>(this.apiProduct, {params});
    }
    getDetailProduct(productId: number){
        return this.http.get(`${this.apiProduct}/${productId}`);
    }
    getProductByIds(ids: number[]){
        const params = new HttpParams()
        .set('ids', ids.join(','));
        return this.http.get<ProductResponse[]>(`${this.apiProduct}/by-ids`, {params});
    }
    createProduct(name: string, price: number,image:string, description: string, category_id: number){
        return this.http.post(this.apiProduct, {name,price,image,description,category_id});
    }
    updateProduct(productId: number, name: string, price: number, description: string, category_id: number){
        debugger
        return this.http.put(`${this.apiProduct}/${productId}`, {name,price,description,category_id});
    }
    deleteProduct(productId: number){
        return this.http.delete(`${this.apiProduct}/${productId}`);
    }

    // hình ảnh
    getImageByName(imageName: String){
        return this.http.get(`${this.apiProduct}/images/${imageName}`);
    }
    uploadImages(productId: number, images: File[]){
        const formData = new FormData();
        images.forEach((image) => {
            debugger
            formData.append(`files`,image);
        });
        return this.http.post(`${this.apiProduct}/uploads/${productId}`, formData,);
    }
    deleteImage(imageId: number){
        return this.http.delete(`${this.apiProduct}/images/${imageId}`);
    }
}