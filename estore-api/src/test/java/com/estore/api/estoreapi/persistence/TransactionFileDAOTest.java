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
import com.estore.api.estoreapi.model.AccountNotFoundException;
import com.estore.api.estoreapi.model.CartProduct;
import com.estore.api.estoreapi.model.InvalidTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence")
public class TransactionFileDAOTest {
    Transaction[] testTransactions;
    ObjectMapper mockObjectMapper;
    TransactionFileDAO mockTransactionFileDAO;
    InventoryFileDAO mockInventoryFileDAO;
    CartFileDAO mockCartFileDAO;

    String validAuth = "test1*1";

    @BeforeEach
    public void setUp() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        mockTransactionFileDAO = mock(TransactionFileDAO.class);
        mockInventoryFileDAO = mock(InventoryFileDAO.class);
        mockCartFileDAO = mock(CartFileDAO.class);
        testTransactions = new Transaction[3];
        CartProduct[] testCartProducts = {new CartProduct(1, 1), new CartProduct(4, 4)};
        CartProduct[] testCartProducts2 = {new CartProduct(2, 2), new CartProduct(5, 5)};
        CartProduct[] testCartProducts3 = {new CartProduct(3, 3), new CartProduct(6, 6)};
        testTransactions[0] = new Transaction(1, 1, testCartProducts, "10", "visa");
        testTransactions[1] = new Transaction(2, 2, testCartProducts2, "20", "debit");
        testTransactions[2] = new Transaction(3, 3, testCartProducts3, "30", "mastercard");

        when(mockObjectMapper.readValue(new File("filenotfound.txt"), Transaction[].class))
                .thenReturn(testTransactions);
        mockTransactionFileDAO = new TransactionFileDAO("filenotfound.txt", mockObjectMapper, mockInventoryFileDAO, mockCartFileDAO);
    }
  
    @Test
    public void testCreateTransaction() throws IllegalArgumentException, AccountNotFoundException, InvalidTokenException {

        try {
            Transaction result = assertDoesNotThrow((() -> mockTransactionFileDAO.createTransaction(testTransactions[0], validAuth)),
                                    "Unexpected exception thrown");

            assertEquals(testTransactions[0].getUser(), mockTransactionFileDAO.createTransaction(testTransactions[0], validAuth).getUser());
            assertEquals(testTransactions[0].getPaymentMethod(), mockTransactionFileDAO.createTransaction(testTransactions[0], validAuth).getPaymentMethod());
            assertEquals(testTransactions[0].getProducts(), mockTransactionFileDAO.createTransaction(testTransactions[0], validAuth).getProducts());
    
        } catch (IOException ioe) {
            
        }
    }
  
    @Test
    public void testGetTransaction() {
        assertEquals(mockTransactionFileDAO.getTransaction(1), testTransactions[0]);
        assertNotEquals(mockTransactionFileDAO.getTransaction(99), testTransactions[0]);
    }

}