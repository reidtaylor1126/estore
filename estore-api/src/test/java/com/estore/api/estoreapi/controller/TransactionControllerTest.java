package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.beans.Transient;
import java.io.IOException;

import com.estore.api.estoreapi.model.Transaction;
import com.estore.api.estoreapi.model.AccountNotFoundException;
import com.estore.api.estoreapi.model.CartProduct;
import com.estore.api.estoreapi.model.InvalidTokenException;
import com.estore.api.estoreapi.persistence.TransactionDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("Controller")
public class TransactionControllerTest {
    private TransactionController transactionController;
    private TransactionDAO mockTransactionDAO;

    String authValid = "test1*1";

    @BeforeEach
    public void setUp() {
        mockTransactionDAO = mock(TransactionDAO.class);
        transactionController = new TransactionController(mockTransactionDAO);

        
    }
  
   @Test
    public void testCreateTransaction() throws IOException, IllegalArgumentException, AccountNotFoundException, InvalidTokenException {
        Integer expected_id = 1;
        Integer expected_user = 2;
        CartProduct[] expected_products = {new CartProduct(1, 1), new CartProduct(4, 4)};
        String expected_dateTime = "3/27";
        String expected_paymentMethod = "visa";

        Transaction transaction = new Transaction(expected_id, expected_user, expected_products, expected_dateTime, expected_paymentMethod);

        when(mockTransactionDAO.createTransaction(transaction, authValid)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction, authValid);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody(), transaction);
    }

    @Test
    public void testCreateTransactionEmptyName() throws IOException, IllegalArgumentException, AccountNotFoundException, InvalidTokenException{
        Integer expected_id = 1;
        Integer expected_user = 2;
        CartProduct[] expected_products = {new CartProduct(1, 1), new CartProduct(4, 4)};
        String expected_dateTime = "3/27";
        String expected_paymentMethod = "";

        Transaction transaction = new Transaction(expected_id, expected_user, expected_products, expected_dateTime, expected_paymentMethod);

        when(mockTransactionDAO.createTransaction(transaction, authValid)).thenReturn(null);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction, authValid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testError() throws IOException, IllegalArgumentException, AccountNotFoundException, InvalidTokenException{
        Integer expected_id = 1;
        Integer expected_user = 2;
        CartProduct[] expected_products = {new CartProduct(1, 1), new CartProduct(4, 4)};
        String expected_dateTime = "3/27";
        String expected_paymentMethod = "visa";

        Transaction transaction = new Transaction(expected_id, expected_user, expected_products, expected_dateTime, expected_paymentMethod);

        when(mockTransactionDAO.createTransaction(transaction, authValid)).thenThrow(new IOException());

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction, authValid);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
  
    @Test
    public void testGetTransaction()
    {
        int testID = 99;
        CartProduct[] products = new CartProduct[3];
        products[0] = new CartProduct(99, 1);
        products[1] = new CartProduct(98, 2);
        products[2] = new CartProduct(97, 3);
        Transaction transaction = new Transaction(99, 99, products, "testDate/Time", "test");

        when(mockTransactionDAO.getTransaction(transaction.getId())).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransaction(testID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetTransactionNotFound()
    {
        int testID = 98;
        CartProduct[] products = new CartProduct[3];
        products[0] = new CartProduct(99, 1);
        products[1] = new CartProduct(98, 2);
        products[2] = new CartProduct(97, 3);
        Transaction transaction = new Transaction(99, 99, products, "testDate/Time", "test");

        when(mockTransactionDAO.getTransaction(transaction.getId())).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransaction(testID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
