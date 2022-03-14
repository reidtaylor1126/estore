package com.estore.api.estoreapi.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {

    static final String STRING_FORMAT = "Product [name=%s, description=%s, price=%f, quantity=%d]";

    @NotNull
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("quantity")
    private Integer quantity;

    public Product(@NotNull @JsonProperty("name") String name,
            @JsonProperty("description") String description, @JsonProperty("price") Double price,
            @JsonProperty("quantity") Integer quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
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
    public Double getPrice() {
        return price;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Product) {
            Product otherProduct = (Product) other;
            return(
                this.name.equals(otherProduct.getName()) &&
                this.description.equals(otherProduct.getDescription()) &&
                this.price == otherProduct.price &&
                this.quantity == otherProduct.quantity
            );
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format(STRING_FORMAT, name, description, price, quantity);
    }
}
