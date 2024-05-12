import { Component } from '@angular/core';
import { OrderService } from '../../../../services/order.service';
import { OrderResponse } from '../../../../responses/order/order.response';
import { OrderDetailResponse } from '../../../../responses/order/order-detail.response';
import { ActivatedRoute } from '@angular/router';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.scss'
})
export class OrderDetailComponent {
  constructor(
    private orderService:OrderService,
    private activatedRoute:ActivatedRoute,
  ) {}
  orderId!:number;
  orderInfo!:OrderResponse;
  listOrderDetail!: OrderDetailResponse[];
  ngOnInit(): void {
    const idParam = this.activatedRoute.snapshot.paramMap.get('id');
    if(idParam!==null)
      {
        // this.productId = +idParam;
        this.orderId = +idParam;
      }
    this.getOrderInfo();
    this.getOrderDetail();
  }
  test(){
    console.log(this.listOrderDetail);
    console.log(this.orderInfo);
    alert(this.orderInfo.payment_method);
  }
  getOrderInfo(){
    this.orderService.getOrderByOrderId(this.orderId).subscribe((data: OrderResponse) => {
      // this.orderInfo = {
      //   ...data,
      //   order_date: new Date(data.order_date)
      // };
      this.orderInfo = data;
    });
  }
  getOrderDetail(){
    this.orderService.getOrderDetailByOrderId(this.orderId).subscribe((data: OrderDetailResponse[]) => {
      data.forEach((order_details: OrderDetailResponse) => {
        order_details.product_image =  `${environment.apiBaseUrl}/products/images/${order_details.product_image}`
      });
      this.listOrderDetail = data;
    });
  }
}
