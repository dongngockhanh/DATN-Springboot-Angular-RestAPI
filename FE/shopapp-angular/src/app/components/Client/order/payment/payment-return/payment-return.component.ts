import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../../../../services/order.service';
import { CartService } from '../../../../../services/cart.service';
// import { Moment } from 'moment';
import moment from 'moment-timezone';

@Component({
  selector: 'app-pament-return',
  templateUrl: './payment-return.component.html',
  styleUrls: ['./payment-return.component.css']
})
export class PaymentReturnComponent implements OnInit {


  constructor(private activeRoute: ActivatedRoute,private orderService:OrderService,private cartService:CartService) { }
  vnp_Amount:any
  vnp_BankCode:any
  vnp_BankTranNo:any
  vnp_CardType:any
  vnp_OrderInfo:any
  vnp_PayDate:any
  vnp_ResponseCode:any
  vnp_TmnCode:any
  vnp_TransactionNo:any
  vnp_TransactionStatus:any
  vnp_TxnRef:any
  vnp_SecureHash:any
  ngOnInit() {
    this.activeRoute.queryParams.subscribe(params => {
      this.vnp_Amount = params['vnp_Amount']/100;
      this.vnp_BankCode = params['vnp_BankCode'];
      this.vnp_BankTranNo = params['vnp_BankTranNo'];
      this.vnp_CardType = params['vnp_CardType'];
      this.vnp_OrderInfo = params['vnp_OrderInfo'];

      //sử dụng thư viện moment để chuyển đổi thời gian
      // Giả sử chuỗi ngày giờ được lưu trữ trong biến 'dateString'
      let dateString = params['vnp_PayDate'];
      console.log(dateString);
      if (dateString) {
        let year = Number(dateString.substring(0, 4));
        let month = Number(dateString.substring(4, 6))-1; // JavaScript counts months from 0
        let day = Number(dateString.substring(6, 8));
        let hour = Number(dateString.substring(8, 10));
        let minute = Number(dateString.substring(10, 12));
        let second = Number(dateString.substring(12, 14));
        
        console.log(year, month, day, hour, minute, second);
        let date = new Date(year, month, day, hour, minute, second);
        this.vnp_PayDate = date.toLocaleString('vi-VN', { timeZone: 'Asia/Ho_Chi_Minh' });
      }

      this.vnp_ResponseCode = params['vnp_ResponseCode'];
      this.vnp_TmnCode = params['vnp_TmnCode'];
      this.vnp_TransactionNo = params['vnp_TransactionNo'];
      this.vnp_TransactionStatus = params['vnp_TransactionStatus'];
      this.vnp_TxnRef = params['vnp_TxnRef'];
      this.vnp_SecureHash = params['vnp_SecureHash'];

      if(this.vnp_ResponseCode === '00'){
        this.createOrder();
      }
    });
  }
  createOrder(){
    const orderData = JSON.parse(localStorage.getItem('orderData')!);
    this.orderService.createOrder(orderData).subscribe({
      next:(response: any) => {
        console.log(response);
        localStorage.removeItem('orderData');
        for(let i=0; i<orderData.cart_items.length; i++){
          this.cartService.removeProduct(orderData.cart_items[i].product_id);
        }
      },
      complete() {
          debugger
      },
      error: (error) => {
        debugger
      }
    });
  }
}
