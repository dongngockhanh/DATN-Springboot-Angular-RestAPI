import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../environments/environment";
import { OrderDTO } from "../dtos/order/order.dto";
@Injectable({
    providedIn: 'root'
})
export class OrderService {
    private apiOrderUrl = `${environment.apiBaseUrl}/orders`;
    constructor(private http: HttpClient) {}
    // tạo mới đơn hàng
    createOrder(order: OrderDTO): Observable<any>{
        return this.http.post(this.apiOrderUrl, order);
    }
}