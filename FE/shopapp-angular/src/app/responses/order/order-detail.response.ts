export interface OrderDetailResponse {
    id: number;
    order_id: number;
    product_id: number;
    product_name: string;
    product_image: string;
    quantity: number;
    price: number;
    total_money: number;
    color: string;
    created_at: number[];
    updated_at: number[];
}