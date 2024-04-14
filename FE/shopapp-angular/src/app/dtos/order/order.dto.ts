import { 
    IsString,
    IsNotEmpty,
    IsPhoneNumber
 } from "class-validator";
export class OrderDTO{
    // thông tin cá nhân
    fullname: string;
    phone_number: string;
    email: string;
    // thông tin nhận hàng
    address: string;
    note: string;
    user_id: string;
    total_money: number;
    shipping_method: string;
    payment_method: string;
    cart_items: any[];
    constructor(data:any){
        this.fullname = '';
        this.phone_number = '';
        this.email = '';
        this.address = '';
        this.note = '';
        this.user_id = '';
        this.total_money = 0;
        this.shipping_method = '';
        this.payment_method = '';
        this.cart_items = [];
    }
}