import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from 'src/app/types/Product';

@Component({
    selector: 'app-product',
    templateUrl: './product.component.html',
    styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {
    constructor(private router: Router) {}

    @Input() product?: Product;

    ngOnInit(): void {}

    inStock(): boolean {
        return (
            typeof this.product?.quantity != 'undefined' &&
            this.product.quantity > 0
        );
    }

    navigate() {
        this.router.navigate([`/products/${this.product?.id}`]);
    }

    addToCart(event: MouseEvent): void {
        event.preventDefault();
        event.stopPropagation();
        //TODO add to cart
    }
}
