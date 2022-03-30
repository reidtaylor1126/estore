import { Product } from "./Product";

export type Transaction = {
    id?: number;
    user: number;
    products: Product[];
    shippingAddress: string;
    dateTime: string;
    paymentMethod: string;
    fulfilled: boolean;
};