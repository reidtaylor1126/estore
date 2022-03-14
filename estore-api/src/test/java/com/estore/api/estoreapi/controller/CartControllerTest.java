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
        new Product(1, "test-1", "test 1", 1.0, 1),
        new Product(2, "test-2", "test 2", 2.0, 2),
        new Product(3, "test-3", "test 3", 3.0, 3)
    };

    private static Cart testCart = new Cart(testProducts);

    private static String authValid = "user1*1";
    private static String authInvalid = "user1*2";
    private static String authNotExist = "user8*6";

    @BeforeEach
    public void setUp() {
        mockCartDAO = mock(CartDAO.class);
        cartController = new CartController(mockCartDAO);
    }

    @Test
    public void testGetCart() throws AccountNotFoundException, InvalidTokenException {
        when(mockCartDAO.getCart(authValid)).thenReturn(testCart);
        ResponseEntity<Cart> responseValid = cartController.getCart(authValid);
        assertEquals(HttpStatus.OK, responseValid.getStatusCode());
        assertEquals(testCart, responseValid.getBody());

        when(mockCartDAO.getCart(authInvalid)).thenThrow(new InvalidTokenException("Token does not match a registered user."));
        ResponseEntity<Cart> responseInvalid = cartController.getCart(authInvalid);
        assertEquals(HttpStatus.FORBIDDEN, responseInvalid.getStatusCode());

        when(mockCartDAO.getCart(authNotExist)).thenThrow(new AccountNotFoundException("User with token does not exist."));
        ResponseEntity<Cart> responseNotExist = cartController.getCart(authNotExist);
        assertEquals(HttpStatus.NOT_FOUND, responseNotExist.getStatusCode());
    }

    @Test
    public void testUpdateCart() throws AccountNotFoundException, InvalidTokenException, IOException {
        when(mockCartDAO.updateCart(authValid, testCart)).thenReturn(testCart);
        ResponseEntity<Cart> responseValid = cartController.updateCart(authValid, testCart);
        assertEquals(HttpStatus.OK, responseValid.getStatusCode());
        assertEquals(testCart, responseValid.getBody());

        when(mockCartDAO.updateCart(authInvalid, testCart)).thenThrow(new InvalidTokenException("Token does not match a registered user."));
        ResponseEntity<Cart> responseInvalid = cartController.updateCart(authInvalid, testCart);
        assertEquals(HttpStatus.FORBIDDEN, responseInvalid.getStatusCode());

        when(mockCartDAO.updateCart(authNotExist, testCart)).thenThrow(new AccountNotFoundException("User with token does not exist."));
        ResponseEntity<Cart> responseNotExist = cartController.updateCart(authNotExist, testCart);
        assertEquals(HttpStatus.NOT_FOUND, responseNotExist.getStatusCode());
    }

    @Test
    public void testUpdateIOException() throws AccountNotFoundException, InvalidTokenException, IOException  {
        when(mockCartDAO.updateCart("", testCart)).thenThrow(new IOException("Artificial IOException"));
        ResponseEntity<Cart> responseIOE = cartController.updateCart("", testCart);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseIOE.getStatusCode());
    }

    @Test
    public void testClearCart() throws AccountNotFoundException, InvalidTokenException, IOException {
        when(mockCartDAO.clearCart(authValid)).thenReturn(testCart);
        ResponseEntity<Cart> responseValid = cartController.clearCart(authValid);
        assertEquals(HttpStatus.OK, responseValid.getStatusCode());
        assertEquals(testCart, responseValid.getBody());

        when(mockCartDAO.clearCart(authInvalid)).thenThrow(new InvalidTokenException("Token does not match a registered user."));
        ResponseEntity<Cart> responseInvalid = cartController.clearCart(authInvalid);
        assertEquals(HttpStatus.FORBIDDEN, responseInvalid.getStatusCode());

        when(mockCartDAO.clearCart(authNotExist)).thenThrow(new AccountNotFoundException("User with token does not exist."));
        ResponseEntity<Cart> responseNotExist = cartController.clearCart(authNotExist);
        assertEquals(HttpStatus.NOT_FOUND, responseNotExist.getStatusCode());
    }

    @Test
    public void testClearIOException() throws AccountNotFoundException, InvalidTokenException, IOException  {
        when(mockCartDAO.clearCart("")).thenThrow(new IOException("Artificial IOException"));
        ResponseEntity<Cart> responseIOE = cartController.clearCart("");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseIOE.getStatusCode());
    }
}
