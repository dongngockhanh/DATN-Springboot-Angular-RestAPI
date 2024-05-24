import { Component } from '@angular/core';
import { ProvincialService } from '../../../services/provincial.service';
import { ProvincialResponse } from '../../../responses/provincial/provincial.response';
import { Province } from '../../../responses/provincial/province';
import { District } from '../../../responses/provincial/district';
import { Ward } from '../../../responses/provincial/ward';
import { Form, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { CartService } from '../../../services/cart.service';
import { ProductService } from '../../../services/productService/product.service';
import { ProductResponse } from '../../../responses/products/product.response';
import { environment } from '../../../environments/environment';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderDTO } from '../../../dtos/order/order.dto';
import { OrderService } from '../../../services/order.service';
import { TokenService } from '../../../services/token.service';
import { MessageService } from 'primeng/api';
import { PaymentService } from '../../../services/payment.service';
import { PaymentDTO } from '../../../dtos/payment/payment.dto';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss',
  providers: [MessageService]
})
export class OrderComponent {
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private cartService: CartService,
    private productService: ProductService,
    private provincialService: ProvincialService,
    private orderService: OrderService,
    private paymentService: PaymentService,
    private tokenService: TokenService,
    private messageService: MessageService,
    private fb: FormBuilder) {

    this.fullname = '';
    this.phonenumber = '';
    this.email = '';
    this.homeaddress = '';
    this.address = '';
    this.note = '';
    this.shippingMethod = this.shippingMethods[0];
    this.paymentMethod = this.paymentMethods[0];

    this.totalMoney = 0;
  }
  ngOnInit() {
    this.initForm();
    this.getProvinces();
    this.getProductByNow();
    localStorage.removeItem('orderData');//xóa orderData trong localStorage
  }

  // lấy dữ liệu từ form
  fullname: string;
  phonenumber: string;
  email: string;
  homeaddress: string;
  address: string;
  note: string;

  // lấy danh sách các tỉnh thành
  provinceId: string = '';
  districtId: string = '';
  formGroup!: FormGroup;
  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  filteredProvinces: Province[] = [];
  filteredDistricts: District[] = [];
  filteredWards: Ward[] = [];

  initForm() {
    this.formGroup = this.fb.group({
      'province': [''],
      'district': [''],
      'ward': ['']
    });
    this.formGroup.get('province')?.valueChanges.subscribe((enteredData: string) => {
      this.fillerDataProvince(enteredData);
    });
    this.formGroup.get('district')?.valueChanges.subscribe((enteredData: string) => {
      this.fillerDataDistrict(enteredData);
    });
    this.formGroup.get('ward')?.valueChanges.subscribe((enteredData: string) => {
      this.fillterDataWard(enteredData);
    });
  }
  fillerDataProvince(enteredData: string) {
    const filteredProvinces = this.provinces.filter(province => {
      return province.province_name.toLowerCase().includes(enteredData.toLowerCase());
    });
    if (filteredProvinces.length === 0) {
      this.filteredProvinces = [];
      this.filteredProvinces.push({ province_id: '', province_name: 'Không tìm thấy tỉnh' });
      console.log(filteredProvinces);
    } else {
      this.filteredProvinces = filteredProvinces;
      const matchedProvince = this.provinces.find(province => province.province_name === enteredData);
      if (matchedProvince) {
        console.log('Tìm thấy tỉnh:', matchedProvince);
        this.formGroup.get('district')?.setValue('');//reset giá trị huyện khi chọn tỉnh khác
        this.formGroup.get('ward')?.setValue('');//reset giá trị phường khi chọn tỉnh khác
        this.provinceId = matchedProvince.province_id;
        console.log('ID tỉnh:', this.provinceId);
        this.filteredDistricts = [];
        this.getDistricts();
        // gán giá trị cho address
        //reset address khi chọn tỉnh khác
        this.address = '';
        this.address = matchedProvince.province_name;
      }
    }
  }
  fillerDataDistrict(enteredData: string) {
    const filteredDistricts = this.districts.filter(district => {
      return district.district_name.toLowerCase().includes(enteredData.toLowerCase());
    });
    if (filteredDistricts.length === 0) {
      this.filteredDistricts = [];
      this.filteredDistricts.push({ district_id: '', district_name: 'Không tìm thấy huyện' });
      console.log(filteredDistricts);
    } else {
      this.filteredDistricts = filteredDistricts;
      const matchedDistrict = this.districts.find(district => district.district_name === enteredData);
      if (matchedDistrict) {
        console.log('Tìm thấy huyện:', matchedDistrict);
        this.formGroup.get('ward')?.setValue('');//reset giá trị phường khi chọn huyện khác
        this.districtId = matchedDistrict.district_id;
        console.log('ID huyện:', this.districtId);
        this.filteredWards = [];
        this.getWards();
        // gán giá trị cho address
        //thay đổi giá trị của address khi chọn huyện khác mà vẫn giữ nguyên giá trị của tỉnh
        this.address = this.address.replace(/(.*),/g,'');
        this.address = `${matchedDistrict.district_name},${this.address}`;
      }
    }
  }
  fillterDataWard(enteredData: string) {
    const filteredWards = this.wards.filter(ward => {
      return ward.ward_name.toLowerCase().includes(enteredData.toLowerCase());
    });
    if (filteredWards.length === 0) {
      this.filteredWards = [];
      this.filteredWards.push({ ward_id: '', ward_name: 'Không tìm thấy phường' });
      console.log(filteredWards);
    } else {
      this.filteredWards = filteredWards;
      const matchedWard = this.wards.find(ward => ward.ward_name === enteredData);
      if (matchedWard) {
        console.log('Tìm thấy phường:', matchedWard);
        const addressParts = this.address.split(',');
        if (addressParts.length > 2) {
          // thay đổi giá trị trước giấu phẩy đầu tiên
          addressParts[0] = matchedWard.ward_name;
          this.address = addressParts.join(',');  
        } else {
          this.address = `${matchedWard.ward_name},${this.address}`
        }
      }
    }
  }

  getProvinces() {
    this.provincialService.getProvinces().subscribe({
      next: (response: any) => {
        this.provinces = response.results;
        this.filteredProvinces = response.results;
      },
    });
  }
  getDistricts() {
    this.provincialService.getDistricts(this.provinceId).subscribe({
      next: (response: any) => {
        // debugger
        this.districts = response.results;
        this.filteredDistricts = response.results;
        console.log(response);
      },
    });
  }
  getWards() {
    this.provincialService.getWards(this.districtId).subscribe({
      next: (response: any) => {
        // debugger
        this.wards = response.results;
        this.filteredWards = response.results;
        console.log(response);
      },
    })
  }
  // đến đây là xong phần lấy dữ liệu tỉnh thành từ API

  // lấy shipping method
  shippingMethod: string;
  shippingMethods: string[] = ['Giao hàng SPX Express', 'Giao hàng nhanh'];
  getShippingMethod(event: Event) {
    const selectedShippingMethod = (event.target as HTMLSelectElement).value;
    this.shippingMethod = selectedShippingMethod;
  }
  // lấy payment method
  paymentMethod: string;
  paymentMethods: string[] = ['Thanh toán khi nhận hàng', 'Thanh toán trực tuyến'];
  getPaymentMethod(event: Event) {
    const selectedPaymentMethod = (event.target as HTMLSelectElement).value;
    this.paymentMethod = selectedPaymentMethod;
  }

  //lấy danh sách sản phảm và tính tổng tiền sản phẩm trong danh sách
  totalMoney: number;
  listId: number[] = [];
  products: ProductResponse[] = [];
  getProductByNow() {
    debugger
    this.route.queryParams.subscribe(params => {
      this.listId = params['ids'].split(',').map(Number);
    });
    this.productService.getProductByIds(this.listId).subscribe({
      next: (response: any) => {
        debugger
        this.products = response.map((product: ProductResponse) => {
          product.image = `${environment.apiBaseUrl}/products/images/${product.image}`;
          product.quantity = this.cartService.getCart().get(product.id) || 1;
          // return product;
        });
        this.products = response;
        this.totalMoney = this.products.reduce((sum, product) => sum + product.price * product.quantity, 0);
      }
    });
  }

  test() {
    // tạo mới đơn hàng
    const orderData: OrderDTO = {
      fullname: this.fullname,
      phone_number: this.phonenumber,
      email: this.email,
      address:this.homeaddress+' '+this.address,
      note: this.note,
      user_id: '27',
      total_money: this.totalMoney,
      shipping_method: this.shippingMethod,
      payment_method: this.paymentMethod,
      cart_items: this.products.map(product => {
        return {
          product_id: product.id,
          quantity: product.quantity
        }
      })
    };
    // debugger
    console.log(orderData);
    console.log(this.homeaddress+' '+this.address);
  }
  // lấy user_id từ localStorage trong assets token
  getUserId(){
    const user_id = this.tokenService.getUserId().toString();
    // console.log(user_id);
    // alert(user_id);
    return user_id ;
  }
  //hiển thị dialog xác nhận lần nữa
  displayDialog: boolean = false;
  showDialog() {
  this.displayDialog = true;
}
  // tạo đơn hàng 
  createOrder() {
    const orderData: OrderDTO = {
      fullname: this.fullname,
      phone_number: this.phonenumber,
      email: this.email,
      address:this.homeaddress+' '+this.address,
      note: this.note,
      user_id: this.getUserId(),
      total_money: this.totalMoney,
      shipping_method: this.shippingMethod,
      payment_method: this.paymentMethod,
      cart_items: this.products.map(product => {
        return {
          product_id: product.id,
          quantity: product.quantity
        }
      })
    };
    if(this.paymentMethod === 'Thanh toán khi nhận hàng'){
    this.orderService.createOrder(orderData).subscribe({
      next: (response: any) => {
        debugger
        console.log(response);
        for(let i = 0; i < this.listId.length; i++){
          this.cartService.removeProduct(this.listId[i]);
        }
        // this.showSuccess('Đặt hàng thành công');
      },
      complete:()=> {
        debugger
        // alert('Đặt hàng thành công');
        this.showSuccess('Đặt hàng thành công');
      },
      error: (error) => {
        debugger
        console.log(error);
      }
    });
    }
    else {
      // alert(this.paymentMethod);
      localStorage.setItem('orderData', JSON.stringify(orderData));//lưu orderData vào localStorage
      this.PaymentVnPay();
    }
  }
  //test thanh toán vnpay
  linkVnPay!: string;
  paymentDto: PaymentDTO ={
    amount: 0 // Initialize totalMoney to a default value
  };
  PaymentVnPay(){
    this.paymentDto.amount = this.totalMoney; // Assign the value of totalMoney to paymentDto.amount
    this.paymentService.createPayment(this.paymentDto).subscribe((paymentResponse:any)=>{
      this.linkVnPay = paymentResponse.url;
      // chuyển hướng
      window.location.href = this.linkVnPay;
    })
  }

  //hiển thị thông báo
  showSuccess(message: string) {
    this.messageService.add({severity:'success', summary: 'Success', detail: message});
  }
}
