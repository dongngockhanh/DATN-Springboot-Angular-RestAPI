import { ProductImage } from "./product-image.response";

export interface ProductResponse {
    id: number;
    name: string;
    price: number;
    image: string | null; // Image có thể là string (đường dẫn) hoặc null
    description: string;
    created_at: number[]; // Mảng chứa các giá trị số nguyên đại diện cho ngày tạo
    updated_at: number[]; // Mảng chứa các giá trị số nguyên đại diện cho ngày cập nhật
    category_id: number;
    product_images: ProductImage[]; // Mảng chứa các hình ảnh của sản phẩm
    quantity: number;
    selected: boolean|false;
}