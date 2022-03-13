package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.model.*;
import com.estore.api.estoreapi.persistence.CartDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("Controller")
public class CartControllerTest {
    private CartController cartController;
    private CartDAO mockCartDAO;

    private static Product[] testProducts = {
        new Product("test-1", "test 1", 1.0, 1),
        new Product("test-2", "test 2", 2.0, 2),
        new Product("test-3", "test 3", 3.0, 3)
    };

    private static Cart testCart = new Cart(testProducts);

    private static UserAccount testUser = new UserAccount(1, "user1", false);

    private static String authValid = "user1*1";
    private static String authInvalid = "user1*2";
    private static String authNotExist = "user8*6";

    @BeforeEach
    public void setUp() {
        mockCartDAO = mock(CartDAO.class);
        cartController = new CartController(mockCartDAO);
    }

    @Test
    public void testGetCartValid() {
        when(mockCartDAO.getCart(authValid)).thenReturn(testCart);
    }
}
