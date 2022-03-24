import { Product } from './Product';

export interface ProductCart {
    products: Product[];
    numItems: number;
    totalPrice: number;
}
