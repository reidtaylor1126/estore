package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartProduct {
    /**
     * The id of the referenced product.
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * The quantity of the product in the cart, not the total quantity in the inventory.
     */
    @JsonProperty("name")
    private Integer quantity;

    private static final String STRING_FORMAT = "CartProduct [id=%d, quantity=%d]";

    public CartProduct(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof CartProduct) {
            CartProduct otherProduct = (CartProduct) other;
            return(this.id == otherProduct.getId() && this.quantity == otherProduct.getQuantity());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, quantity);
    }
}
