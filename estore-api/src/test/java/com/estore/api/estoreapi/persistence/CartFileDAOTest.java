package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.estore.api.estoreapi.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence")
public class CartFileDAOTest {
    private CartFileDAO cartFileDAO;
    private ObjectMapper mockObjectMapper;
    private UserDAO mockUserDAO;

    private UserAccount mockUser = new UserAccount(1, "user1", false);

    private Product[] testProducts0 = {
        new Product("test-1", "test 1", 1.0, 2)
    };

    private Product[] testProducts1 = {
        new Product("test-1", "test 1", 1.0, 1),
        new Product("test-2", "test 2", 2.0, 2),
        new Product("test-3", "test 3", 3.0, 3)
    };

    private Cart testCart0 = new Cart(testProducts0);
    private Cart testCart1 = new Cart(testProducts1);

    private String mockToken = "token";

    @BeforeEach
    public void init() throws IOException, AccountNotFoundException, InvalidTokenException {
        mockObjectMapper = mock(ObjectMapper.class);
        mockUserDAO = mock(UserDAO.class);
        when(mockObjectMapper.readValue(new File("data/carts/1.json"), Product[].class)).thenReturn(new Product[0]);
        cartFileDAO = new CartFileDAO(mockObjectMapper, mockUserDAO);
        when(mockUserDAO.verifyToken(mockToken)).thenReturn(mockUser);
    }

    @Test
    public void testCreateCart() throws IOException, AccountNotFoundException, InvalidTokenException {
        Cart result = cartFileDAO.createCart(mockUser, testCart0);

        assertEquals(testCart0, result);
        assertEquals(testCart0, cartFileDAO.getCart(mockToken));
    }

    @Test
    public void testUpdateCart() throws IOException, AccountNotFoundException, InvalidTokenException {
        Cart result = cartFileDAO.updateCart(mockToken, testCart1);

        assertEquals(testCart1, result);
        assertEquals(testCart1, cartFileDAO.getCart(mockToken));
    }

    @Test
    public void testClearCart() throws IOException, AccountNotFoundException, InvalidTokenException {
        cartFileDAO.createCart(mockUser, testCart1);

        Cart result = cartFileDAO.clearCart(mockToken);

        assertEquals(testCart1, result);
        assertEquals(Cart.EMPTY, cartFileDAO.getCart(mockToken));
    }

    @Test
    public void deleteCart() throws IOException, AccountNotFoundException, InvalidTokenException {
        cartFileDAO.createCart(mockUser, testCart1);

        Cart result = cartFileDAO.deleteCart(mockToken);

        assertEquals(testCart1, result);
        File cartFile = new File("data/carts/1.json");
        assertEquals(false, cartFile.exists());
    }
}
