import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CartService } from 'src/app/services/cart/cart.service';
import { InventoryService } from 'src/app/services/inventory/inventory.service';
import { Product } from 'src/app/types/Product';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  @Input() product: Product = {
    name: "Placeholder",
    description: "Placeholder description",
    price: 0.0,
    quantity: 1,
  };

  @Input() localQuantity: number = 1;

  private userIsAdmin: boolean = false;

  private name: string = "";
  constructor(private route: ActivatedRoute, private inventoryService: InventoryService, private cartService: CartService) {
    this.name = String(this.route.snapshot.paramMap.get('name'));
  }

  ngOnInit(): void {
    this.inventoryService.getProduct(this.name).subscribe((value) => {
      var tempQuantity = this.product.quantity != 1 ? this.product.quantity : 1;
      this.product = value;
      this.product.quantity = tempQuantity;
    })
  }

  getImage(): string {
    return "https://source.unsplash.com/500x500?cards";
    // Placeholder until cards have their own images
  }

  isEditing(): boolean {
    return this.userIsAdmin;
  }

  addToCart(event: MouseEvent): void {
    if(this.localQuantity <= this.product.quantity) {
      this.cartService.addToCart({
        name: this.product.name,
        description: this.product.description,
        price: this.product.price,
        quantity: this.localQuantity
      });
      this.product.quantity = 1;
    }
  }

  inStock(): boolean {
    return this.product.quantity > 0;
  }
}
