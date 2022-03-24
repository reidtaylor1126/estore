import { CartProduct } from './CartProduct';

export interface Cart {
    products: CartProduct[];
    numItems: number;
    totalPrice: number;
}
