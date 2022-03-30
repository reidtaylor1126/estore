package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import com.estore.api.estoreapi.model.Transaction;
import com.estore.api.estoreapi.model.UserAccount;
import com.estore.api.estoreapi.model.AccountNotFoundException;
import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.model.CartProduct;
import com.estore.api.estoreapi.model.InvalidTokenException;
import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence")
public class TransactionFileDAOTest {
    Transaction[] testTransactions;
    ObjectMapper mockObjectMapper;
    TransactionFileDAO transactionFileDAO;
    InventoryFileDAO mockInventoryFileDAO;
    Transaction mockTransaction;
    CartFileDAO mockCartFileDAO;
    UserFileDAO mockUserFileDAO;
    UserAccount mockUser;
    Cart mockCart;

    String validAuth = "test1*1";

    @BeforeEach
    public void setUp() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        mockTransaction = mock(Transaction.class);
        mockInventoryFileDAO = mock(InventoryFileDAO.class);
        mockCartFileDAO = mock(CartFileDAO.class);
        mockUserFileDAO = mock(UserFileDAO.class);
        mockUser = mock(UserAccount.class);
        mockCart = mock(Cart.class);
        testTransactions = new Transaction[3];
        Product[] testCartProducts = {new Product(1, "Prod1", "Prod1", 1.99, 1), new Product(2, "Prod2", "Prod2", 2.99, 2)};
        Product[] testCartProducts2 = {new Product(3, "Prod3", "Prod3", 3.99, 3), new Product(4, "Prod4", "Prod4", 4.99, 4)};
        Product[] testCartProducts3 = {new Product(5, "Prod5", "Prod5", 5.99, 5), new Product(6, "Prod6", "Prod6", 6.99, 6)};
        testTransactions[0] = new Transaction(1, 1, testCartProducts, "10", "visa", "address");
        testTransactions[1] = new Transaction(2, 2, testCartProducts2, "20", "debit", "address2");
        testTransactions[2] = new Transaction(3, 3, testCartProducts3, "30", "mastercard", "address3");

        when(mockObjectMapper.readValue(new File("filenotfound.txt"), Transaction[].class))
                .thenReturn(testTransactions);
        transactionFileDAO = new TransactionFileDAO("filenotfound.txt", mockObjectMapper, mockInventoryFileDAO, mockCartFileDAO, mockUserFileDAO);
    }
  
    @Test
    public void testCreateTransaction() throws IllegalArgumentException, AccountNotFoundException, InvalidTokenException {

        try {

            when(mockInventoryFileDAO.confirmTransaction(testTransactions[0])).thenReturn(true);
            when(mockCartFileDAO.getCart(validAuth)).thenReturn(mockCart);
            when(mockUserFileDAO.verifyToken(validAuth)).thenReturn(mockUser);
            
            Transaction result = assertDoesNotThrow((() -> transactionFileDAO.createTransaction(validAuth, "visa", "address")),
            "Unexpected exception thrown");

            assertNotNull(result);
            
            System.out.println(result);
                                    
            assertEquals(testTransactions[0].getUser(), result.getUser());
            assertEquals(testTransactions[0].getPaymentMethod(), result.getPaymentMethod());
            assertEquals(testTransactions[0].getProducts(), result.getProducts());
    
        } catch (IOException ioe) {
            
        }
    }
  
    @Test
    public void testGetTransaction() {
        assertEquals(transactionFileDAO.getTransaction(1), testTransactions[0]);
        assertNotEquals(transactionFileDAO.getTransaction(99), testTransactions[0]);
    }

}