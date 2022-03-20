package com.estore.api.estoreapi.model;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Reid Taylor
 * 
 * The cart class represents a currently active cart, storing a list of references to products in the inventory
 */
public class Cart {
    @NotNull
    @JsonProperty
    private CartProduct[] products;

    @JsonProperty
    private int numItems;

    public static final Cart EMPTY = new Cart();
    private static final String STRING_FORMAT = "Cart [numItems=%d]";

    public Cart() {
        this.products = new CartProduct[0];
        this.numItems = 0;
    }

    public Cart(CartProduct[] products) {
        this.products = products;
        this.numItems = getNumItems();
    }

    /**
     * Returns an array of the {@link Product}s in this cart
     * @return an array of the {@link Product}s in this cart
     */
    public CartProduct[] getProducts() {
        return this.products;
    }

    /**
     * Returns the number of items in the cart, taking multiples into consideration
     * @return the total number of items in the cart
     */
    public int getNumItems() {
        int total = 0;
        for (CartProduct product : this.products) {
            total += product.getQuantity();
        }

        return total;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Cart) {
            Cart otherCart = (Cart) other;
            return(
                Arrays.equals(this.products, otherCart.getProducts())
            );
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format(STRING_FORMAT, numItems);
    }
}