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
    searchQuery: string = '';
    constructor(private inventoryService: InventoryService) {}

    ngOnInit(): void {
        this.inventoryService.getItems().subscribe((value) => {
            this.products = value;
        });
    }

    updateSearch() {
        if (this.searchQuery.length > 0) {
            this.inventoryService
                .searchItems(this.searchQuery)
                .subscribe((value) => {
                    this.products = value;
                });
        } else {
            this.clearSearch();
        }
    }

    clearSearch() {
        this.searchQuery = '';
        this.inventoryService.getItems().subscribe((value) => {
            this.products = value;
        });
    }

    hasProducts(): boolean {
        if (this.products) {
            return this.products.length > 0;
        }
        return false;
    }
}
