import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { InventoryService } from 'src/app/services/inventory/inventory.service';
import { Product, ProductNoId } from 'src/app/types/Product';

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

    image: File | undefined;

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

        const product: ProductNoId = {
            name: this.formData.name,
            description: this.formData.description,
            price: Number(this.formData.price),
            quantity: Number(this.formData.quantity),
            numImages: 0,
        };

        this.inventoryService.createProduct(product).subscribe((value) => {
            if (this.image && value.id) {
                this.inventoryService
                    .addImage(this.image, value.id)
                    .subscribe(() => {
                        this.router.navigate(['/products']);
                    });
            } else {
                this.router.navigate(['/products']);
            }
        });
    }

    onFileSelected(event: any): void {
        if (event.target.files && event.target.files.length) {
            const file = event.target.files[0];
            if (file.size > 5000000) {
                alert('File is too large. Plase select a smaller file.');
                return;
            }
            if (file.type !== 'image/jpeg') {
                alert('File is not a jpeg. Please select a jpeg file.');
                return;
            }
            this.image = file;
        }
    }

    getImageName(): string {
        if (this.image) {
            return this.image.name;
        }
        return '';
    }
}
