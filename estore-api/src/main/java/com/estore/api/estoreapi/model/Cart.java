package com.estore.api.estoreapi.model;

public interface Cart {
    
    /**
     * Returns an array of the {@link Product}s in this cart
     * @return an array of the {@link Product}s in this cart
     */
    public Product[] getProducts();

    /**
     * Returns the number of items in the cart, taking multiples into consideration
     * @return the total number of items in the cart
     */
    public int getNumItems();

    /**
     * Returns the total price of all of the items in the cart
     * @return
     */
    public Double getTotalPrice();

}
