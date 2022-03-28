export type ProductNoId = {
    name: string;
    description: string;
    price: number;
    quantity: number;
    numImages: number;
};
export type Product = {
    id: number;
    name: string;
    description: string;
    price: number;
    quantity: number;
    numImages: number;
};

export type ProductNoImage = {
    id?: number;
    name: string;
    description: string;
    price: number;
    quantity: number;
};
