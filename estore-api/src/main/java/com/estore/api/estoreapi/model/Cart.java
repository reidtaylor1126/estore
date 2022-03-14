package com.estore.api.estoreapi.model;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cart {
    @NotNull
    @JsonProperty
    private Product[] products;

    @JsonProperty
    private int numItems;

    @JsonProperty
    private Double totalPrice;

    public static final Cart EMPTY = new Cart();
    private static final String STRING_FORMAT = "Cart [numItems=%d, totalPrice=%.2f]";

    public Cart() {
        this.products = new Product[0];
        this.numItems = 0;
        this.totalPrice = 0.0;
    }

    public Cart(Product[] products) {
        this.products = products;
        this.numItems = getNumItems();
        this.totalPrice = getTotalPrice();
    }

    /**
     * Returns an array of the {@link Product}s in this cart
     * @return an array of the {@link Product}s in this cart
     */
    public Product[] getProducts() {
        return this.products;
    }

    /**
     * Returns the number of items in the cart, taking multiples into consideration
     * @return the total number of items in the cart
     */
    public int getNumItems() {
        int total = 0;
        for (Product product : products) {
            total += product.getQuantity();
        }

        return total;
    }

    /**
     * Returns the total price of all of the items in the cart
     * @return
     */
    public Double getTotalPrice() {
        Double total = 0.0;
        for (Product product : products) {
            total += product.getPrice()*product.getQuantity();
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
        return String.format(STRING_FORMAT, numItems, totalPrice);
    }
}