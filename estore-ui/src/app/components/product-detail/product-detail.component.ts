import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { InventoryService } from 'src/app/services/inventory/inventory.service';
import { Product } from 'src/app/types/Product';

@Component({
    selector: 'app-product-detail',
    templateUrl: './product-detail.component.html',
    styleUrls: ['./product-detail.component.css'],
})
export class ProductDetailComponent implements OnInit {
    @Input() product: Product = {
        id: -1,
        name: 'Placeholder',
        description: 'Placeholder description',
        price: 0.0,
        quantity: 1,
    };

    @Input() localQuantity: number = 1;

    private userIsAdmin: boolean = false;
    private editing: boolean = false;
    private isNullValue = false;

    private id: number = -1;
    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private inventoryService: InventoryService,
        private cartService: CartService,
        private authService: AuthService
    ) {
        this.id = parseInt(String(this.route.snapshot.paramMap.get('id')));
    }

    ngOnInit(): void {
        this.inventoryService.getProduct(this.id).subscribe({
            next: (value) => {
                this.product = value;
            },
            error: (err) => {
                this.isNullValue = true;
            },
        });
        this.authService.currentUser.subscribe((value) => {
            if (value) {
                this.userIsAdmin = value.admin;
            }
        });
    }

    startEditing(): void {
        if (this.userIsAdmin) this.editing = true;
    }

    saveEdit(): void {
        if (this.isAdmin()) {
            //console.log('Attempting to save edits...');
            this.inventoryService
                .updateProduct(this.product)
                .subscribe((value) => {
                    this.product = value;
                });
        }
        this.leaveEditing();
    }

    isNull(): boolean {
        return this.isNullValue;
    }

    cancelEdit(): void {
        if (this.isAdmin()) {
            this.inventoryService.getProduct(this.id).subscribe((value) => {
                this.product = value;
            });
        }
        this.leaveEditing();
    }

    leaveEditing = () => {
        this.editing = false;
    };

    deleteProduct(): void {
        if (this.userIsAdmin) {
            this.inventoryService.deleteProduct(this.id).subscribe(() => {
                this.router.navigateByUrl('/products');
            });
        }
    }

    getImage(): string {
        return 'https://source.unsplash.com/500x500?cards';
        // Placeholder until cards have their own images
    }

    isEditing(): boolean {
        return this.userIsAdmin && this.editing;
    }

    isAdmin(): boolean {
        return this.userIsAdmin;
    }

    addToCart(event: MouseEvent): void {
        if (this.localQuantity <= this.product.quantity) {
            this.cartService.addToCart({
                id: this.product.id,
                name: this.product.name,
                description: this.product.description,
                price: this.product.price,
                quantity: this.localQuantity,
            });
            this.product.quantity = 1;
        }
    }

    inStock(): boolean {
        return this.product.quantity > 0;
    }
}
