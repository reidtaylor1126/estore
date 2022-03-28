import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Product, ProductNoId, ProductNoImage } from 'src/app/types/Product';

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

    updateProduct(product: ProductNoImage): Observable<Product> {
        return this.httpClient.put<Product>('/api/inventory', product);
    }

    deleteProduct(id: number): Observable<Product> {
        return this.httpClient.delete<Product>(`/api/inventory/${id}`);
    }

    searchItems(query: string): Observable<Product[]> {
        return this.httpClient.get<Product[]>(`/api/inventory?q=${query}`);
    }

    createProduct(product: ProductNoId): Observable<Product> {
        return this.httpClient.post<Product>('/api/inventory', product).pipe(
            map((p) => {
                console.log(p);
                return p;
            })
        );
    }

    addImage(file: File, productId: number): Observable<void> {
        const formData = new FormData();
        formData.append('product', productId.toString());
        formData.append('image', file);
        return this.httpClient.put<void>('/api/inventory/image', formData);
    }

    deleteImage(productId: number, imageId: number): Observable<void> {
        return this.httpClient.delete<void>(
            `/api/inventory/image?productId=${productId}&imageId=${imageId}`
        );
    }
}
