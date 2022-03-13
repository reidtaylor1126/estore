package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model")
public class CartTest {
    private static Product[] testProducts = {
        new Product("test-1", "test 1", 1.0, 1),
        new Product("test-2", "test 2", 2.0, 2),
        new Product("test-3", "test 3", 3.0, 3)
    };

    @Test
    public void testCreate() {
        Cart testCart = new Cart(testProducts);

        assertArrayEquals(testProducts, testCart.getProducts());
        assertEquals(6, testCart.getNumItems());
        assertEquals(14.0, testCart.getTotalPrice());
    }

    @Test
    public void testCreateEmpty() {
        Cart emptyCart = new Cart();

        assertArrayEquals(new Product[0], emptyCart.getProducts());
        assertEquals(0, emptyCart.getNumItems());
        assertEquals(0.0, emptyCart.getTotalPrice());
    }
}
