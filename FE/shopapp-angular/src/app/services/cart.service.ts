import { Inject, Injectable } from "@angular/core";
import { ProductService } from "./productService/product.service";
import { environment } from "../environments/environment";
import { HttpClient } from "@angular/common/http";
import { StorageService } from "./storage.service";
import { TokenService } from "./token.service";
@Injectable({
    providedIn: 'root'
})
export class CartService {
    private apiCartUrl = `${environment.apiBaseUrl}/carts`;
    private cart: Map<number, number> = new Map();
    constructor(
        private productService: ProductService,
        private httpClient: HttpClient,
        private tokenService: TokenService,
    ) {
        // luôn lấy dữ liệu giỏ hàng thực tế trong database khi khởi tạo cart service
        this.getcartfromDB(this.tokenService.getUserId());
        // lấy dữ liệu từ localStorage khi khởi tạo service
        const storedCart = localStorage.getItem('cart');
        if (storedCart) {
            this.cart = new Map(JSON.parse(storedCart));
            // {
            //     "2": 3,
            //     "3": 1
            // }
        }
    }
    // lấy dữ liệu cart từ database và thêm vào cart sau đó lưu vào localstorage
    getcartfromDB(userId: number) {
        this.httpClient.get(`${this.apiCartUrl}/${userId}`).subscribe({
            next: (response: any) => {
                this.cart = new Map();
                for (let i = 0; i < response.length; i++) {
                    this.cart.set(response[i].product_id, response[i].quantity);
                }
                this.saveCartToLocalStorage();
            },
            error: (error: any) => {
                debugger
                console.error(error);
            }
        });
    }
    // thêm sản phẩm vào giỏ hàng trong database
    addToCartDB(user_id: number, product_id: number, quantity: number = 1) {
        this.httpClient.post(this.apiCartUrl, { user_id, product_id, quantity }).subscribe({
            next: (response: any) => {
                console.log(response);
            },
            error: (error: any) => {
                console.error(error);
            }
        });
    }
    // xoá 1 sản phẩm trong giỏ hàng của database
    removeProductDB(user_id: number, product_id: number) {
        this.httpClient.delete(`${this.apiCartUrl}/${user_id}/${product_id}`).subscribe({
            next: (response: any) => {
                console.log(response);
            },
            error: (error: any) => {
                console.error(error);
            }
        });
    }

    // thêm sản phẩm vào giỏ hàng tạm trong localstorage
    addToCart(productId: number, quantity: number = 1): void {
        debugger
        if (this.cart.has(productId)) {
            // nếu đã có sản phẩm trong giỏ hàng thì cộng thêm số lượng
            this.cart.set(productId, this.cart.get(productId)! + quantity);
        } else {
            // nếu chưa có sản phẩm trong giỏ hàng thì thêm mới sản phẩm có số lượng là quantity
            this.cart.set(productId, quantity);
        }
        this.saveCartToLocalStorage(); //lưu sau khi thay đổi
        //lưu vào database
        this.addToCartDB(this.tokenService.getUserId(), productId, this.cart.get(productId));

    }
    getCart(): Map<number, number> {
        return this.cart;
    }
    // cập nhật số lượng sản phẩm trong giỏ hàng
    updateCart(productId: number, quantity: number): void {
        if (this.cart.has(productId)) {
            this.cart.set(productId, quantity);
            this.saveCartToLocalStorage();
            this.addToCartDB(this.tokenService.getUserId(), productId, quantity);
        }
    }
    // lưu giỏ hàng vào localStorage
    private saveCartToLocalStorage(): void {
        debugger
        localStorage.setItem('cart', JSON.stringify(Array.from(this.cart.entries())));
    }
    // xóa hết sản phẩm khỏi giỏ hàng trong localstorage
    clearCart(): void {
        this.cart.clear();
        this.saveCartToLocalStorage();
    }
    // xóa 1 sản phẩm khỏi giỏ hàng
    removeProduct(productId: number): void {
        debugger
        this.cart.delete(productId);
        this.saveCartToLocalStorage();
        this.removeProductDB(this.tokenService.getUserId(), productId);
    }
}