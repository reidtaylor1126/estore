package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author Ryan Yocum
 * 
 *         The Product class represents a product in the inventory.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {

    /**
     * The format for the Product toString method.
     */
    static final String STRING_FORMAT =
            "Product [id=%s, name=%s, description=%s, price=%f, quantity=%d]";

    /**
     * The id of the product.
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * The name of the product.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The description of the product.
     */
    @JsonProperty("description")
    private String description;

    /**
     * The price of the product.
     */
    @JsonProperty("price")
    private Double price;

    /**
     * The quantity of the product.
     */
    @JsonProperty("quantity")
    private Integer quantity;

    /**
     * Constructor.
     *
     * @param id the id of the product
     * @param name the name of the product
     * @param description the description of the product
     * @param price the price of the product
     * @param quantity the quantity of the product
     */
    public Product(@JsonProperty("id") Integer id, @JsonProperty("name") String name,
            @JsonProperty("description") String description, @JsonProperty("price") Double price,
            @JsonProperty("quantity") Integer quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * @return the id
     */
    public Integer getId() {
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
    public Double getPrice() {
        return price;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @return the string representation of the product
     */
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
        return String.format(STRING_FORMAT, id, name, description, price, quantity);
    }
}
