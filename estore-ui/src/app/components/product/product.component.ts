import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { Product } from 'src/app/types/Product';

@Component({
    selector: 'app-product',
    templateUrl: './product.component.html',
    styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {
    constructor(
        private router: Router,
        private cartService: CartService,
        private authService: AuthService
    ) {}

    @Input() product?: Product;
    imgSource: string = '';

    ngOnInit(): void {
        this.imgSource =
            this.product?.id !== undefined
                ? `/api/inventory/image?productId=${this.product.id}`
                : '/api/inventory/image?productId=-1&imageId=-1';
    }

    inStock(): boolean {
        return (
            typeof this.product?.quantity != 'undefined' &&
            this.product.quantity > 0
        );
    }

    navigate() {
        this.router.navigate([`/products/${this.product?.id}`]);
    }

    isAdmin() {
        return this.authService.getCurrentUser()?.admin === true;
    }

    addToCart(event: MouseEvent): void {
        event.preventDefault();
        event.stopPropagation();
        if (!this.product) return;
        this.cartService.addToCart(this.product).subscribe();
    }

    getProductImage(): string {
        return this.imgSource;
    }
}
