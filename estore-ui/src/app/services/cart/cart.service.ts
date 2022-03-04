import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Product } from 'src/app/types/Product';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor() { }

  private numItems: number = 0;

  addToCart(product: Product): Observable<Product[]> {
    this.numItems += product.quantity;
    // Placeholder until cart is done on backend
    return of([]);
  }

  getNumItems(): number {
    return this.numItems;
  }
}
