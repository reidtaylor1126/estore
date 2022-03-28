/**
 * @author Nathan (Nate) Appleby npa1508
 */

package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.model.*;
import com.estore.api.estoreapi.persistence.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("Controller")
public class TransactionControllerTest {
    private TransactionController transactionController;
    private TransactionDAO mockTransactionDAO;

    @BeforeEach
    public void setUp()
    {
        mockTransactionDAO = mock(TransactionDAO.class);
        transactionController = new TransactionController(mockTransactionDAO);
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
