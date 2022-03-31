import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
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
        numImages: 0,
    };

    @Input() localQuantity: number = 1;
    private userIsAdmin: boolean = false;
    private editing: boolean = false;
    private currentImage: number = 0;
    imgSource: string = '';
    private imageLoaded: boolean = false;
    private stagedImageActions: {
        type: 'add' | 'delete';
        productId: number;
        imageId: number;
        imageFile?: File;
    }[] = [];
    private imgLocations: Map<number, { imageId: number; imgSource: string }> =
        new Map();

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
        this.inventoryService.getProduct(this.id).subscribe((value) => {
            this.product = value;
            this.initImages();
        });
        this.authService.currentUser.subscribe((value) => {
            if (value) {
                this.userIsAdmin = value.admin;
            }
        });
    }

    initImages(): void {
        for (let i = 0; i < this.product.numImages; i++) {
            this.imgLocations.set(i, {
                imageId: i,
                imgSource: `/api/inventory/image?productId=${this.product.id}&imageId=${i}`,
            });
        }
        this.imgSource =
            this.product?.id !== undefined
                ? `/api/inventory/image?productId=${this.product.id}`
                : '/api/inventory/image?productId=-1&imageId=-1';
        this.imageLoaded = true;
    }

    startEditing(): void {
        if (this.userIsAdmin) this.editing = true;
    }

    saveEdit(): void {
        if (this.isAdmin()) {
            const actions: (() => Promise<void>)[] = [];
            for (const stagedAction of this.stagedImageActions) {
                if (
                    stagedAction.type === 'add' &&
                    this.product.id !== undefined &&
                    stagedAction.imageFile !== undefined
                ) {
                    actions.push(
                        () =>
                            new Promise<void>((resolve, reject) => {
                                if (
                                    stagedAction.imageFile !== undefined &&
                                    this.product.id !== undefined
                                ) {
                                    this.inventoryService
                                        .addImage(
                                            stagedAction.imageFile,
                                            this.product.id
                                        )
                                        .subscribe((value) => {
                                            resolve(value);
                                        });
                                }
                            })
                    );
                } else if (
                    stagedAction.type === 'delete' &&
                    this.product.id !== undefined &&
                    stagedAction.imageId !== undefined
                ) {
                    actions.push(
                        () =>
                            new Promise<void>((resolve, reject) => {
                                if (this.product.id !== undefined) {
                                    this.inventoryService
                                        .deleteImage(
                                            this.product.id,
                                            stagedAction.imageId
                                        )
                                        .subscribe((value) => {
                                            resolve(value);
                                        });
                                }
                            })
                    );
                }
            }

            (async () => {
                for (const action of actions) {
                    await action();
                }
                this.initImages();
                this.stagedImageActions = [];
                this.goToImg(
                    this.currentImage < this.product.numImages
                        ? this.currentImage
                        : this.product.numImages - 1
                );

                this.inventoryService
                    .updateProduct({
                        id: this.product.id,
                        name: this.product.name,
                        description: this.product.description,
                        price: this.product.price,
                        quantity: this.product.quantity,
                    })
                    .subscribe((value) => {
                        this.product = value;
                    });
            })();
        }
        this.leaveEditing();
    }

    cancelEdit(): void {
        const additons = this.stagedImageActions.filter((value) => {
            return value.type === 'add';
        });
        const deletions = this.stagedImageActions.filter((value) => {
            return value.type === 'delete';
        });
        this.product.numImages =
            this.product.numImages + deletions.length - additons.length;

        this.imgLocations.clear();
        for (let i = 0; i < this.product.numImages; i++) {
            this.imgLocations.set(i, {
                imageId: i,
                imgSource: `/api/inventory/image?productId=${this.product.id}&imageId=${i}`,
            });
        }

        if (this.currentImage >= this.product.numImages) {
            this.currentImage = this.product.numImages - 1;
        }

        this.stagedImageActions = [];
        this.imgSource =
            this.getImgLocation(this.currentImage) ||
            '/api/inventory/image?productId=-1&imageId=-1';

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

    isImageLoaded(): boolean {
        return this.imageLoaded;
    }

    getCurrentImg(): number {
        return this.currentImage;
    }

    isEditing(): boolean {
        return this.userIsAdmin && this.editing;
    }

    isAdmin(): boolean {
        return this.userIsAdmin;
    }

    getImgLocation(id: number): string | undefined {
        const img = this.imgLocations.get(id);
        if (img === undefined && this.product.numImages === 0) {
            return `/api/inventory/image?productId=-1&imageId=-1`;
        }
        return this.imgLocations.get(id)?.imgSource;
    }

    addToCart(event: MouseEvent): void {
        if (this.localQuantity <= this.product.quantity) {
            this.cartService
                .addToCart(this.product, this.localQuantity)
                .subscribe();
        }
    }

    inStock(): boolean {
        return this.product.quantity > 0;
    }

    stageDeleteImage(): void {
        if (this.product.id !== undefined) {
            const currentImage = this.getCurrentImg();
            const addImg = this.stagedImageActions.find((value) => {
                return value.type === 'add' && value.imageId === currentImage;
            });
            if (addImg !== undefined) {
                this.stagedImageActions = this.stagedImageActions.filter(
                    (value) => {
                        return value.imageId !== currentImage;
                    }
                );
            } else {
                this.stagedImageActions?.push({
                    type: 'delete',
                    productId: this.product.id,
                    imageId: currentImage,
                });
            }
            this.imgLocations.delete(this.getCurrentImg());
            let i = 0;
            const newImgLocations = new Map<
                number,
                { imageId: number; imgSource: string }
            >();
            for (const [key, value] of this.imgLocations) {
                if (key > this.getCurrentImg()) {
                    const newValue = value;
                    newValue.imageId = value.imageId - 1;
                    newImgLocations.set(key - 1, newValue);
                } else {
                    newImgLocations.set(key, value);
                }
                i++;
            }
            this.product.numImages--;
            this.imgLocations = newImgLocations;
            this.goToImg(
                this.getCurrentImg() >= this.product.numImages
                    ? 0
                    : this.getCurrentImg()
            );
        }
    }

    nextImg(): void {
        if (this.product.numImages > 0) {
            this.currentImage =
                (this.currentImage + 1) % this.product.numImages;
            const img = this.getImgLocation(this.currentImage);
            if (img !== undefined) {
                this.imgSource = img;
            }
        }
    }

    prevImg(): void {
        if (this.product.numImages > 0) {
            this.currentImage =
                (this.currentImage - 1 + this.product.numImages) %
                this.product.numImages;
            const img = this.getImgLocation(this.currentImage);
            if (img !== undefined) {
                this.imgSource = img;
            }
        }
    }

    goToImg(id: number): void {
        this.currentImage = id;
        const img = this.getImgLocation(this.currentImage);
        if (img !== undefined) {
            this.imgSource = img;
        }
    }

    onFileChange(event: any): void {
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
            if (this.product.id !== undefined) {
                this.stagedImageActions?.push({
                    type: 'add',
                    productId: this.product.id,
                    imageId: this.product.numImages,
                    imageFile: file,
                });

                // Create url from file
                const reader = new FileReader();
                reader.onload = (e: any) => {
                    this.imgLocations.set(this.product.numImages, {
                        imageId: this.product.numImages,
                        imgSource: e.target.result,
                    });
                    this.product.numImages++;
                    this.goToImg(this.product.numImages - 1);
                    const input = document.getElementById('file-input') as any;
                    if (input) {
                        input.value = null;
                    }
                };
                reader.readAsDataURL(file);
            }
        }
    }
}
