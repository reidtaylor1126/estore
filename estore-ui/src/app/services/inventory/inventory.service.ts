import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from 'src/app/types/Product';

@Injectable({
    providedIn: 'root',
})
export class InventoryService {
    constructor(private httpClient: HttpClient) {}

    getItems(): Observable<Product[]> {
        return this.httpClient.get<Product[]>('/api/inventory');
    }

    getProduct(id: number): Observable<Product> {
        return this.httpClient.get<Product>(`/api/inventory/${id}`);
    }

    updateProduct(product: Product): Observable<Product> {
        return this.httpClient.put<Product>('/api/inventory', product);
    }

    deleteProduct(id: number): Observable<Product> {
        return this.httpClient.delete<Product>(`/api/inventory/${id}`);
    }
}
