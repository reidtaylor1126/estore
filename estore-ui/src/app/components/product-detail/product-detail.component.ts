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
    private imageFile: string = 'https://source.unsplash.com/500x500?cards';
    private newImage: string = '';
    private imageUpdated: boolean = false;
    private updatedImageFile?: File;
    imgSource: string = '';
    private imageLoaded: boolean = false;

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
        console.log(this.product.id);
        this.inventoryService.getProduct(this.id).subscribe((value) => {
            this.product = value;
            this.imgSource =
                this.product?.id !== undefined
                    ? `/api/inventory/image?id=${this.product.id}`
                    : 'https://source.unsplash.com/500x500?cards';
            this.imageLoaded = true;
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
            if (this.imageUpdated && this.updatedImageFile && this.product.id) {
                this.inventoryService
                    .uploadImage(this.updatedImageFile, this.product.id)
                    .subscribe((value) => {
                        this.imageFile = value;
                    });
            }
            this.inventoryService
                .updateProduct(this.product)
                .subscribe((value) => {
                    this.product = value;
                });
        }
        this.leaveEditing();
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
        return this.imageFile;
    }

    getNewImage(): string {
        return this.newImage;
    }

    onFileSelected(event: Event): void {
        const fileArr: FileList | null = (event.target as HTMLInputElement)
            .files;
        if (!fileArr) return;
        const file: File = fileArr[0];
        if (file) {
            if (!file.type.match('image.*')) return;
            if (file.size > 10000000) return;
            // Create url to display uploaded image
            const reader: FileReader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => {
                this.imgSource = reader.result as string;
            };
            this.newImage = file.name;
            this.imageUpdated = true;
            this.updatedImageFile = file;
        }
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

    imageError(): string {
        if (!this.imageLoaded) return 'Loading...';
        console.log('Image error');
        this.imgSource = 'https://source.unsplash.com/500x500?cards';
        return 'https://source.unsplash.com/500x500?cards';
    }
}
