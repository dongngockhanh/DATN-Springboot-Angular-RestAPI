import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../../services/order.service';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-order-admin',
  templateUrl: './order-admin.component.html',
  styleUrls: ['./order-admin.component.css']
})
export class OrderAdminComponent implements OnInit {

  listOrder : any;
  statusOrder = [
    "pending","shipping","shipped","completed"
  ];

  constructor(private orderService: OrderService){

  }

  ngOnInit(): void {
    this.getListOrder();
  }


  getListOrder(){
    this.orderService.getListOrder().subscribe({
      next: res=>{
        this.listOrder = res;
        console.log(this.listOrder);
      },error: err =>{
        console.log(err);
      }
    })
  }
  changeStatusOrder(index:any){
    this.listOrder[index].status = "shipping";
  }

  exportToExcel() {
    const formattedOrders = this.listOrder.map((order:any) => {
      return {
        'ID': order.id,
        'Full Name': order.fullname,
        'Email': order.email,
        'Phone Number': order.phone_number,
        'Address': order.address,
        'Note': order.note,
        'Status': order.status,
        'Order Date': new Date(order.order_date).toLocaleDateString(),
        'Total Money': order.total_money,
        'Shipping Method': order.shipping_method,
        'Payment Method': order.payment_Method,
        'Shipping Date': order.shipping_date.join('/'),
        'Tracking Number': order.tracking_number,
        'Shipping Address': order.shipping_address
      };
    });

    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(formattedOrders);
    const workbook: XLSX.WorkBook = { Sheets: { 'data': worksheet }, SheetNames: ['data'] };
    const excelBuffer: any = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
    this.saveExcelFile(excelBuffer, 'order_list');
  }

  private saveExcelFile(buffer: any, fileName: string): void {
    const data: Blob = new Blob([buffer], { type: 'application/octet-stream' });
    const a: HTMLAnchorElement = document.createElement('a');
    a.href = window.URL.createObjectURL(data);
    a.download = fileName + '.xlsx';
    a.click();
  }

}
