import { Component, OnInit } from '@angular/core';
import { InventoryService } from 'src/app/services/inventory/inventory.service';
import { Product } from 'src/app/types/Product';

@Component({
    selector: 'app-products',
    templateUrl: './products.component.html',
    styleUrls: ['./products.component.css'],
})
export class ProductsComponent implements OnInit {
    products?: Product[];
    constructor(private inventoryService: InventoryService) {}

    ngOnInit(): void {
        this.inventoryService.getItems().subscribe((value) => {
            this.products = value;
        });
    }
}
