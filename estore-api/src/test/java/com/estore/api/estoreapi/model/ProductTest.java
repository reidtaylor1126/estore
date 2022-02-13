package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model")
public class ProductTest {
    @Test
    public void testCreate() {
        String expected_name = "test";
        String expected_description = "test";
        double expected_price = 99.99;
        int expected_quantity = 99;
        Product product = new Product(expected_name, expected_description, expected_price,
                expected_quantity);
        assert product.getName().equals(expected_name);
        assert product.getDescription().equals(expected_description);
        assert product.getPrice() == expected_price;
        assert product.getQuantity() == expected_quantity;
    }

    @Test
    public void testName() {
        String name = "test";
        String description = "test";
        double price = 99.99;
        int quantity = 99;
        String expected_name = "new name";
        Product product = new Product(name, description, price, quantity);

        product.setName(expected_name);

        assert product.getName().equals(expected_name);

    }

    @Test
    public void testToString() {
        String name = "test";
        String description = "test";
        double price = 99.99;
        int quantity = 99;
        Product product = new Product(name, description, price, quantity);
        String expected_string = String.format(Product.STRING_FORMAT, name, description, price, quantity);
        assert product.toString().equals(expected_string);
    }
}
