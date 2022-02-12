package com.estore.api.estoreapi.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    static final String STRING_FORMAT = "Product [id=%d, name=%s, description=%s, price=%f, quantity=%d]";

    @NotNull
    @JsonProperty("id")
    private int id;

    @NotNull
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private double price;

    @JsonProperty("quantity")
    private int quantity;

    public Product(@NotNull @JsonProperty("id") int id, @NotNull @JsonProperty("name") String name,
            @JsonProperty("description") String description, @JsonProperty("price") double price,
            @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, name, description, price, quantity);
    }
}
