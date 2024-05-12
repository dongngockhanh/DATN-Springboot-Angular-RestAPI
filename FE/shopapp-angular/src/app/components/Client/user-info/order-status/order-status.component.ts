import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { OrderResponse } from '../../../../responses/order/order.response';
import { OrderService } from '../../../../services/order.service';
import { TokenService } from '../../../../services/token.service';

@Component({
  selector: 'app-order-status',
  templateUrl: './order-status.component.html',
  styleUrl: './order-status.component.scss'
})
export class OrderStatusComponent {
  constructor(private router:Router, private orderService: OrderService,private tokenService: TokenService) {
  }
  userId!:number;
  listOrder!: OrderResponse[];
  // danh sách trạng thái đơn hàng: pending, confirmation, processing, shipping, shipped, completed, canceled. có dạng key-value
  statusOrderToVN: { [key: string]: string } = {
    'pending': 'Đang chờ xác nhận',
    'confirmation': 'Đã xác nhận',
    'processing': 'Đang xử lý',
    'shipping': 'Đang vận chuyển',
    'shipped': 'Sản phẩm đã được giao',
    'completed': 'Hoàn thành',
    'canceled': 'Đã hủy',
  };
  
  ngOnInit(): void {
    this.userId = this.tokenService.getUserId();
    this.getListOrderOfUser();
  }
  getListOrderOfUser(){
    this.orderService.getOrderByUserId(this.userId).subscribe((data: OrderResponse[]) => {
      this.listOrder = data;
    });
  }
  isStepCompleted(currentStatus: string, stepStatus: string): boolean {
    const statusOrder = ['pending', 'confirmation', 'processing', 'shipping', 'shipped', 'completed', 'canceled'];
    return statusOrder.indexOf(currentStatus) >= statusOrder.indexOf(stepStatus);
  }
}
