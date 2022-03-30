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

    @JsonProperty("shippingAddress")
    private String shippingAddress;

    @JsonProperty("fulfilled")
    private Boolean fulfilled;

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

    public String getShippingAddress() {
        return shippingAddress;
    }

    public boolean getFulfilledStatus() {
        return fulfilled;
    }

    public void changeFulfilledStatus(Boolean bool) {
        fulfilled = bool;
    }

    public static final String STRING_FORMAT = "Transaction [id=%d, user=%d Date/Time=%s Payment Method=%s Shipping Address=%s Products Purchased=%s Fulfilled=%b]";

    public Transaction(@JsonProperty("id") int id, @JsonProperty("user") int user, @JsonProperty("products") CartProduct[] products, @JsonProperty("dateTime") String dateTime, @JsonProperty("paymentMethod") String paymentMethod, 
                            @JsonProperty("shippingAddress") String shippingAddress) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.dateTime = dateTime;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.fulfilled = false;
    }

    public Transaction(int user, CartProduct[] products, String paymentMethod) {
        id = -1;
        this.user = user;
        this.products = products;
        dateTime = "N/A";
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        String productList = "";

        for (CartProduct product : products) {
            productList = productList + product.toString() + ", ";
        }

        return String.format(STRING_FORMAT, id, user, dateTime, paymentMethod, shippingAddress, productList, fulfilled);
    }
}
