import { Inject } from "@angular/core";
import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { PaymentDTO } from "../dtos/payment/payment.dto";
@Injectable({
    providedIn: 'root'
})
export class PaymentService {
    private apiPaymentUrl = `${environment.apiBaseUrl}/payment`;
    constructor(private http: HttpClient) { }
    createPayment(payment: PaymentDTO) {
        // const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
        const params = new HttpParams().set('amount', payment.amount.toString());
        return this.http.get(this.apiPaymentUrl,{params}); 
    }
}