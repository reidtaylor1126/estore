package com.estore.api.estoreapi.model;

import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {
    /**
     * The id of the transaction.
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * User attached to transaction
     */
    @JsonProperty("user")
    private Integer user;

    /**
     * Products for transaction
     */
    @JsonProperty("products")
    private CartProduct[] products;

    /**
     * Date and Time of transaction
     */
    @JsonProperty("dateTime")
    private String dateTime;

    /**
     * Payment method to pay for transaction
     */
    @JsonProperty("paymentMethod")
    private String paymentMethod;

    public Integer getId() {
        return id;
    }

    public Integer getUser() {
        return user;
    }

    public CartProduct[] getProducts() {
        return products;
    }

    public String getDateTime() {
        return dateTime;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }

    private static final String STRING_FORMAT = "Transaction [id=%d, user=%d Date/Time=%s Payment Method=%s Products Purchased=%s]";

    public Transaction(@JsonProperty("id") int id, @JsonProperty("user") int user, @JsonProperty("products") CartProduct[] products, @JsonProperty("dateTime") String dateTime, @JsonProperty("paymentMethod") String paymentMethod) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.dateTime = dateTime;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        String productList = "";

        for (CartProduct product : products) {
            productList = productList + product.toString() + ", ";
        }

        return String.format(STRING_FORMAT, id, user, dateTime, paymentMethod, productList);
    }
}