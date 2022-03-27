import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { InventoryService } from 'src/app/services/inventory/inventory.service';
import { Product } from 'src/app/types/Product';

@Component({
    selector: 'app-create-product',
    templateUrl: './create-product.component.html',
    styleUrls: ['./create-product.component.css'],
})
export class CreateProductComponent implements OnInit {
    formData = {
        name: '',
        description: '',
        price: '',
        quantity: '',
    };

    constructor(
        private inventoryService: InventoryService,
        private router: Router
    ) {}

    ngOnInit(): void {}

    createProduct(): void {
        if (
            this.formData.name === '' ||
            this.formData.description === '' ||
            this.formData.price === '' ||
            this.formData.quantity === ''
        )
            return;

        const product: Product = {
            name: this.formData.name,
            description: this.formData.description,
            price: Number(this.formData.price),
            quantity: Number(this.formData.quantity),
            numImages: 0,
        };

        this.inventoryService.createProduct(product).subscribe(() => {
            this.router.navigate(['/products']);
        });
    }
}
