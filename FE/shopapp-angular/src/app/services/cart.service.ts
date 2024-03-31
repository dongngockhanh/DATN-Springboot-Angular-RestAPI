import { Inject, Injectable } from "@angular/core";
import { ProductService } from "./productService/product.service";
@Injectable({
    providedIn: 'root'
})
export class CartService {
    private cart: Map<number,number> = new Map();
    constructor(private productService: ProductService){
        // lấy dữ liệu từ localStorage khi khởi tạo service
        const storedCart = localStorage.getItem('cart');
        if(storedCart){
            this.cart = new Map(JSON.parse(storedCart));
            // {
            //     "2": 3,
            //     "3": 1
            // }
        }
    }
    addToCart(productId: number, quantity: number = 1): void{
        debugger
        if(this.cart.has(productId))
        {
            // nếu đã có sản phẩm trong giỏ hàng thì cộng thêm số lượng
            this.cart.set(productId, this.cart.get(productId)! + quantity);
        }else{
            // nếu chưa có sản phẩm trong giỏ hàng thì thêm mới sản phẩm có số lượng là quantity
            this.cart.set(productId, quantity);
        }
        this.saveCartToLocalStorage(); //lưu sau khi thay đổi
    }
    getCart(): Map<number,number>{
        return this.cart;
    }
    // cập nhật số lượng sản phẩm trong giỏ hàng
    updateCart(productId: number, quantity: number): void{
        if(this.cart.has(productId)){
            this.cart.set(productId, quantity);
            this.saveCartToLocalStorage();
        }
    }
    // lưu giỏ hàng vào localStorage
    private saveCartToLocalStorage(): void{
        debugger    
        localStorage.setItem('cart', JSON.stringify(Array.from(this.cart.entries())));
    }
    // xóa hết sản phẩm khỏi giỏ hàng
    clearCart(): void{
        this.cart.clear();
        this.saveCartToLocalStorage();
    }
    // xóa 1 sản phẩm khỏi giỏ hàng
    removeProduct(productId: number): void{
        debugger
        this.cart.delete(productId);
        this.saveCartToLocalStorage();
    }
}