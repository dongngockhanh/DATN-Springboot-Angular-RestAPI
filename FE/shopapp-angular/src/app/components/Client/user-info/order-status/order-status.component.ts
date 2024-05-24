import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { OrderResponse } from '../../../../responses/order/order.response';
import { OrderService } from '../../../../services/order.service';
import { TokenService } from '../../../../services/token.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-order-status',
  templateUrl: './order-status.component.html',
  styleUrl: './order-status.component.scss',
  providers: [MessageService]
})
export class OrderStatusComponent {
  constructor(
    private messageService: MessageService,
    private router:Router, 
    private orderService: OrderService,
    private tokenService: TokenService) {
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
      // thay đổi ngày đặt hàng trong order
      this.listOrder.forEach((order: OrderResponse) => {
        let date = new Date(order.order_date);
        date.setDate(date.getDate() + 4);
        order.order_date = date;
      });
    });
  }
  getExpectedDate(orderDate: string): Date {
    let date = new Date(orderDate);
    date.setDate(date.getDate() + 4);
    return date;
  }
  //css cho trạng thái đơn hàng
  isStepCompleted(currentStatus: string, stepStatus: string): boolean {
    const statusOrder = ['pending', 'confirmation', 'processing', 'shipping', 'shipped', 'completed', 'canceled'];
    return statusOrder.indexOf(currentStatus) >= statusOrder.indexOf(stepStatus);
  }
  isDialogVisible: boolean = false;
  showDialog(orderId: number, tracking_order: string) {
    this.isDialogVisible = true;
    this.orderId = orderId;
    this.tracking_order = tracking_order;
  }
  tracking_order!: string;
  orderId!: number;
  cancelOrder(){
    this.orderService.cancelOrder(this.orderId).subscribe((data) => {
      this.showSuccess('Đã hủy đơn hàng');
      this.getListOrderOfUser();
    });
  }
  //show message
  showSuccess(text: string) {
    this.messageService.add({severity:'success', summary: 'Success', detail: text});
  }
  showError(text: string) {
    this.messageService.add({severity:'error', summary: 'Error', detail: text});
  }
}
