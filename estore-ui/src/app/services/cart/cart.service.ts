import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom, map, Observable, of } from 'rxjs';
import { Cart } from 'src/app/types/Cart';
import { CartProduct } from 'src/app/types/CartProduct';
import { Product } from 'src/app/types/Product';
import { ProductCart } from 'src/app/types/ProductCart';
import { AuthService } from '../auth/auth.service';
import { InventoryService } from '../inventory/inventory.service';

@Injectable({
    providedIn: 'root',
})
export class CartService {
    private numItems = new BehaviorSubject<number>(0);
    private cart = new BehaviorSubject<Cart | null>(null);
    private numItems: number = 0;
    constructor(
        private authService: AuthService,
        private httpClient: HttpClient,
        private inventoryService: InventoryService
    ) {}
    addToCart(product: Product, quantity?: number): Observable<Cart | null> {
        const token = this.authService.getToken();
        if (!token) return of(null);
        if (product.id === undefined) return of(null);
        const cartData = {
            id: product.id,
            quantity: quantity || 1,
        };
        let cartValue = this.getCartValue();
        if (cartValue === null) {
            cartValue = {
                products: [],
                numItems: 0,
                totalPrice: 0,
            };
        }
        const cart = this.addToCartHelper(cartValue, cartData);
        return this.updateCart(cart).pipe(
            map((cart) => {
                this.cart.next(cart);
                return cart;
            })
        );
    }

    getNumItems(): number {
        return this.numItems.value;
    }

    getNumItemsObservable(): Observable<number> {
        return this.numItems.asObservable();
    }

    getCartValue(): Cart | null {
        return this.cart.value;
    }

    getCartObservable(): Observable<Cart | null> {
        return this.cart.asObservable();
    }

    private getCart(token: string): Observable<Cart> {
        return this.httpClient
            .get<Cart>('/api/cart', {
                headers: { token: token },
            })
            .pipe(
                map((cart) => {
                    return cart;
                })
            );
    }

    // private function that puts a CartProduct into the Cart and if the product is already in the cart, it increments the quantity
    private addToCartHelper(cart: Cart, product: CartProduct): Cart {
        console.log(cart.products);
        console.log(product);
        const cartProduct = cart.products.find((p) => p.id === product.id);
        if (cartProduct) {
            console.log('here');
            cartProduct.quantity += product.quantity;
        } else {
            cart.products.push(product);
        }
        cart.numItems += product.quantity;
        return cart;
    }
    deleteItem(productId: number | undefined): void {
        const cart = this.getCartValue();
        if (cart && productId !== undefined) {
            const newProducts = cart.products.filter((product) => {
                return product.id !== productId;
            });
            this.updateCart({
                ...cart,
                products: newProducts,
            }).subscribe((cart) => {
                this.cart.next(cart);
            });
        }
    }

    convertToCart(productCart: ProductCart): Cart {
        const cart: Cart = {
            products: [],
            numItems: 0,
            totalPrice: 0,
        };
        productCart.products.forEach((product) => {
            if (product.id !== undefined) {
                cart.products.push({
                    id: product.id,
                    quantity: product.quantity,
                });
                cart.numItems += product.quantity;
                cart.totalPrice += product.price * product.quantity;
            }
        });
        return cart;
    }

    updateCart(cart: Cart): Observable<Cart | null> {
        const token = this.authService.getToken();
        if (!token) return of(null);
        return this.httpClient
            .put<Cart>('/api/cart', cart.products, {
                headers: { token: token },
            })
            .pipe(
                map((cart) => {
                    this.numItems.next(cart.numItems);
                    this.cart.next(cart);
                    return cart;
                })
            );
    }

    async getCartProducts(): Promise<ProductCart | null> {
        const token = this.authService.getToken();
        if (!token) return null;
        const cart = await firstValueFrom(this.getCart(token));
        this.cart.next(cart);
        this.numItems.next(cart.numItems);
      
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
            return {
                ...productData,
                quantity: product.quantity,
            };
            return productData;
        });
        return {
            products: await Promise.all(products),
            totalPrice: cart.totalPrice,
            numItems: cart.numItems,
        };
    }
}
