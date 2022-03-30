import { Component, Input, OnInit } from '@angular/core';
import { InventoryService } from 'src/app/services/inventory/inventory.service';
import { CartProduct } from 'src/app/types/CartProduct';
import { Product } from 'src/app/types/Product';

@Component({
  selector: 'app-transaction-product',
  templateUrl: './transaction-product.component.html',
  styleUrls: ['./transaction-product.component.css']
})
export class TransactionProductComponent implements OnInit {
  @Input() cartProduct?: CartProduct;

  product?: Product;
  totalPrice?: number;

  constructor(private inventoryService: InventoryService) {}

  ngOnInit(): void {
    const id = this.cartProduct == undefined? -1 : this.cartProduct.id;
    const quantity = this.cartProduct == undefined? 1 : this.cartProduct.quantity;
    this.inventoryService.getProduct(id).subscribe((product) => {
      this.product = product;
      this.product.quantity = quantity;
      this.totalPrice = quantity * product.price;
    })
  }
}
