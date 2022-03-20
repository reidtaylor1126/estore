package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model")
public class CartTest {
    private static CartProduct[] testProducts = {
        new CartProduct(1, 1),
        new CartProduct(3, 2)
    };

    @Test
    public void testCreate() {
        Cart testCart = new Cart(testProducts);

        assertArrayEquals(testProducts, testCart.getProducts());
        assertEquals(3, testCart.getNumItems());
    }

    @Test
    public void testCreateEmpty() {
        Cart emptyCart = new Cart();

        assertArrayEquals(new Product[0], emptyCart.getProducts());
        assertEquals(0, emptyCart.getNumItems());
    }
}
