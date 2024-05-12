export interface OrderResponse {
    id: number;
    fullname: string;
    phone_number: string;
    email: string;
    address: string;
    note: string;
    user_id: number;
    total_money: number;
    order_date: Date;
    status: string;
    shipping_method: string;
    payment_method: string;
    shipping_date: Date;
    tracking_number: string;
    shipping_address: string;
    created_at: number[];
    updated_at: number[];
}