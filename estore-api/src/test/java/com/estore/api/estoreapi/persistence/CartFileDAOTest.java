package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@Tag("Persistence")
public class CartFileDAOTest {
    private ObjectMapper mockObjectMapper;
    private static UserFileDAO mockUserDAO;
    private static InventoryFileDAO mockInventoryDAO;

    private UserAccount mockUser = new UserAccount(9999, "user9999", false);

    // Maintained for context since CartProducts contain no information about the product they reference
    // private Product[] testInventory = {
    //     new Product(1, "test-1", "test 1", 1.0, 1),
    //     new Product(2, "test-2", "test 2", 2.0, 2),
    //     new Product(3, "test-3", "test 3", 3.0, 3)
    // };

    private static CartProduct[] testProducts0 = {
        new CartProduct(1, 1),
        new CartProduct(3, 2)
    };

    private static CartProduct[] testProducts1 = {
        new CartProduct(1, 1),
        new CartProduct(3, 2)
    };
    
    private static CartProduct[] testProducts2 = {
        new CartProduct(1, 1),
        new CartProduct(4, 2)
    };

    private Cart testCart0 = new Cart(testProducts0);
    private Cart testCart1 = new Cart(testProducts1);

    private String mockToken = "token";

    private static MockedStatic<UserFileDAO> mockStaticUFD;
    private static MockedStatic<InventoryFileDAO> mockStaticIFD;

    @BeforeAll
    public static void init() {
        mockUserDAO = mock(UserFileDAO.class);
        mockInventoryDAO = mock(InventoryFileDAO.class);
        mockStaticUFD = Mockito.mockStatic(UserFileDAO.class);
        mockStaticIFD = Mockito.mockStatic(InventoryFileDAO.class);
        mockStaticUFD.when(UserFileDAO::getInstance).thenReturn(mockUserDAO);
        mockStaticIFD.when(InventoryFileDAO::getInstance).thenReturn(mockInventoryDAO);
    }

    @BeforeEach
    public void prepare() throws IOException, AccountNotFoundException, InvalidTokenException {
        mockObjectMapper = mock(ObjectMapper.class);
        when(mockObjectMapper.readValue(new File("data/test-carts/test-instance/9999.json"), CartProduct[].class)).thenReturn(testProducts0);
        when(mockObjectMapper.readValue(new File("data/test-carts/test-instance/9999.json"), CartProduct[].class)).thenReturn(testProducts0);
        when(mockUserDAO.verifyToken(mockToken)).thenReturn(mockUser);
    }

    @Test
    public void testCreateCart() throws IOException, AccountNotFoundException, InvalidTokenException {
        CartFileDAO cartFileDAO = new CartFileDAO(mockObjectMapper, "data/carts-tests/test-create");
        Cart result = cartFileDAO.createCart(mockUser, testCart0);

        assertEquals(testCart0, result);
        assertEquals(testCart0, cartFileDAO.getCart(mockToken));

        File cartFile = new File("data/carts-tests/test-create/9999.json");
        cartFile.delete();
    }

    @Test
    public void testUpdateCart() throws IOException, AccountNotFoundException, InvalidTokenException {
        CartFileDAO cartFileDAO = new CartFileDAO(mockObjectMapper, "data/carts-tests/test-update");
        cartFileDAO.createCart(mockUser, Cart.EMPTY);
        Cart result = cartFileDAO.updateCart(mockToken, testCart1);

        assertEquals(testCart1, result);
        assertEquals(testCart1, cartFileDAO.getCart(mockToken));

        File cartFile = new File("data/carts-tests/test-update/9999.json");
        cartFile.delete();
    }

    @Test
    public void testClearCart() throws IOException, AccountNotFoundException, InvalidTokenException {
        CartFileDAO cartFileDAO = new CartFileDAO(mockObjectMapper, "data/carts-tests/test-clear");
        cartFileDAO.createCart(mockUser, testCart0);

        Cart result = cartFileDAO.clearCart(mockToken);

        assertEquals(testCart1, result);
        assertEquals(Cart.EMPTY, cartFileDAO.getCart(mockToken));
        File cartFile = new File("data/carts-tests/test-clear/9999.json");
        cartFile.delete();
    }

    @Test
    public void deleteCart() throws IOException, AccountNotFoundException, InvalidTokenException {
        CartFileDAO cartFileDAO = new CartFileDAO(mockObjectMapper, "data/carts-tests/test-delete");
        cartFileDAO.createCart(mockUser, testCart0);

        Cart result = cartFileDAO.deleteCart(mockToken);

        assertEquals(testCart1, result);
        
        File cartFile = new File("data/carts-tests/test-delete/9999.json");
        assertEquals(false, cartFile.exists());
    }
}
