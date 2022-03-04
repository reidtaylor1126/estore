import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from 'src/app/types/Product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  constructor(private httpClient: HttpClient) { }

  getProduct(name: string): Observable<Product> {
    console.log(`fetching product ${name}`);
    return this.httpClient.get<Product>(`http://localhost:8080/inventory/product/${name}`);
  }
}
