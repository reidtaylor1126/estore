import { Component, OnInit } from '@angular/core';
import { CartService } from 'src/app/services/cart/cart.service';
import { ProductCart } from 'src/app/types/ProductCart';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html',
    styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {
    private productCart: ProductCart | undefined;

    constructor(private cartService: CartService) {}

    async ngOnInit(): Promise<void> {
        this.productCart = await this.cartService.getCartProducts();
    }

    getProductCart(): ProductCart | undefined {
        return this.productCart;
    }
}
