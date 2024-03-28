import { Injectable, numberAttribute } from "@angular/core";
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { ProductResponse } from "../../responses/prodducts/product.response";
import { environment } from "../../environments/environment";
import { catchError, Observable, throwError } from "rxjs";
@Injectable({
    providedIn: 'root'
})
export class ProductService{
    private apiProduct = `${environment.apiBaseUrl}/products`;
    constructor(private http: HttpClient) { }

    getProducts(keyword:string, categoryid:number, page : number,limit : number): Observable<ProductResponse[]>{
        debugger
        const params = new HttpParams()
        .set('keyword', keyword)
        .set('category_id', categoryid.toString())
        .set('page', page.toString())
        .set('limit', limit.toString());
        return this.http.get<ProductResponse[]>(this.apiProduct, {params});
    }
}