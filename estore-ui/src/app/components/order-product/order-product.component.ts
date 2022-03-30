import { Component, Input, OnInit } from '@angular/core';
import { Product } from 'src/app/types/Product';

@Component({
  selector: 'app-order-product',
  templateUrl: './order-product.component.html',
  styleUrls: ['./order-product.component.css']
})
export class OrderProductComponent implements OnInit {

@Input() product?: Product;

  constructor() { }

  ngOnInit(): void {
  }

}
