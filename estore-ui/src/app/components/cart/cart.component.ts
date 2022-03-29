import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { first } from 'rxjs';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { ProductCart } from 'src/app/types/ProductCart';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html',
    styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {
    private productCart: ProductCart | null;

    constructor(
        private cartService: CartService,
        private authService: AuthService,
        private router: Router
    ) {
        this.productCart = null;
    }

    async ngOnInit(): Promise<void> {
        if (!this.authService.getCurrentUser()) {
            this.router.navigate(['/login'], {
                queryParams: { redirect: '/cart' },
            });
        }
        this.cartService
            .getCartObservable()
            .pipe(first())
            .subscribe((value) => {
                this.cartService.getCartProducts().then((productCart) => {
                    this.productCart = productCart;
                });
            });
    }

    deleteItem(productId?: number): void {
        if (!this.productCart) return;
        this.cartService.deleteItem(productId);
        this.productCart.products = this.productCart.products.filter(
            (product) => {
                return product.id !== productId;
            }
        );
        this.productCart.totalPrice = this.productCart.products.reduce(
            (acc, product) => acc + product.price,
            0
        );
    }

    getTotalPrice(): number {
        if (this.productCart) {
            return this.productCart.totalPrice;
        }
        return 0;
    }

    getTotalQuantity(): number {
        if (this.productCart) {
            return this.productCart.numItems;
        }
        return 0;
    }

    hasProducts(): boolean {
        if (this.productCart && this.productCart.products.length > 0) {
            return true;
        }
        return false;
    }

    getProductCart(): ProductCart | null {
        return this.productCart;
    }

    updateCart(): void {
        if (this.productCart) {
            this.productCart.totalPrice = this.productCart.products.reduce(
                (acc, product) => acc + product.price,
                0
            );
            this.cartService
                .updateCart(this.cartService.convertToCart(this.productCart))
                .subscribe(async (cart) => {
                    const productCart =
                        await this.cartService.getCartProducts();
                    this.productCart = productCart;
                });
        }
    }
}
