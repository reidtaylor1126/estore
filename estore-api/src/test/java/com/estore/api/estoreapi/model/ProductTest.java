package com.estore.api.estoreapi.model;

import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

@Tag("Model")
public class ProductTest {
    @Test
    public void testCreate() {
        Integer expected_id = 1;
        String expected_name = "test";
        String expected_description = "test";
        double expected_price = 99.99;
        int expected_quantity = 99;
        Integer expected_numImages = 1;
        Product product = new Product(expected_id, expected_name, expected_description,
                expected_price, expected_quantity, expected_numImages);
        assert product.getId() == expected_id;
        assert product.getName().equals(expected_name);
        assert product.getDescription().equals(expected_description);
        assert product.getPrice() == expected_price;
        assert product.getQuantity() == expected_quantity;
        assert product.getNumImages().equals(expected_numImages);
    }

    @Test
    public void testName() {
        Integer id = 1;
        String name = "test";
        String description = "test";
        double price = 99.99;
        int quantity = 99;
        String expected_name = "new name";
        Integer numImages = 1;
        Product product = new Product(id, name, description, price, quantity, numImages);

        product.setName(expected_name);

        assert product.getName().equals(expected_name);

    }

    @Test
    public void testToString() {
        Integer id = 1;
        String name = "test";
        String description = "test";
        double price = 99.99;
        int quantity = 99;
        Integer numImages = 1;
        Product product = new Product(id, name, description, price, quantity, numImages);
        String expected_string = String.format(Product.STRING_FORMAT, id, name, description, price,
                quantity, numImages);
        assert product.toString().equals(expected_string);
    }
}
