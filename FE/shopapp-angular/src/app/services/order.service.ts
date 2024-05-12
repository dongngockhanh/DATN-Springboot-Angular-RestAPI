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
    getListOrder(): Observable<any>{
        return this.http.get(this.apiOrderUrl);
    }
    getOrderByUserId(userId: number): Observable<any>{
        return this.http.get(`${this.apiOrderUrl}/user/${userId}`);
    }
    getOrderByOrderId(orderId: number): Observable<any>{
        return this.http.get(`${this.apiOrderUrl}/${orderId}`);
    }
    getOrderDetailByOrderId(orderId: number): Observable<any>{
        return this.http.get(`${environment.apiBaseUrl}/order_details/order/${orderId}`);
    }
}