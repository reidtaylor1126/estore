import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, map, Observable, of } from 'rxjs';
import { Cart } from 'src/app/types/Cart';
import { Product } from 'src/app/types/Product';
import { ProductCart } from 'src/app/types/ProductCart';
import { AuthService } from '../auth/auth.service';
import { InventoryService } from '../inventory/inventory.service';

@Injectable({
    providedIn: 'root',
})
export class CartService {
    private numItems: number = 0;
    constructor(
        private authService: AuthService,
        private httpClient: HttpClient,
        private inventoryService: InventoryService
    ) {}

    addToCart(product: Product): Observable<Product[]> {
        this.numItems += product.quantity;
        // Placeholder until cart is done on backend
        return of([]);
    }

    getNumItems(): number {
        return this.numItems;
    }

    getCart(): Observable<Cart> {
        const token = this.authService.getToken();
        if(token != undefined) {
            return this.httpClient.get<Cart>('/api/cart', {
                headers: {'token': token}
            });
        } else {
            return new Observable<Cart>();
        }
    }

    async getCartProducts(): Promise<ProductCart> {
        const cart = await firstValueFrom(this.getCart());
        const products = cart.products.map(async (product) => {
            const productData = await firstValueFrom(
                this.inventoryService.getProduct(product.id)
            );
            return productData;
        });
        return {
            products: await Promise.all(products),
            totalPrice: cart.totalPrice,
            numItems: cart.numItems,
        };
    }
}
