package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionInfo {
    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("shippingAddress")
    private String shippingAddress;

    public TransactionInfo(@JsonProperty("paymentMethod") String paymentMethod, @JsonProperty("shippingAddress") String shippingAddress)
    {
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod()
    {
        return paymentMethod;
    }

    public String getShippingAddress()
    {
        return shippingAddress;
    }
}
