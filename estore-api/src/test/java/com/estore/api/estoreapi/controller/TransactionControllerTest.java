package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.beans.Transient;
import java.io.IOException;

import com.estore.api.estoreapi.model.*;
import com.estore.api.estoreapi.model.Transaction;
import com.estore.api.estoreapi.model.TransactionInfo;
import com.estore.api.estoreapi.model.UserAccount;
import com.estore.api.estoreapi.model.AccountNotFoundException;
import com.estore.api.estoreapi.model.CartProduct;
import com.estore.api.estoreapi.model.InvalidTokenException;
import com.estore.api.estoreapi.persistence.TransactionDAO;
import com.estore.api.estoreapi.persistence.UserFileDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("Controller")
public class TransactionControllerTest {
    private TransactionController transactionController;
    private TransactionDAO mockTransactionDAO;
    private UserAccount mockUser;
    private UserFileDAO mockUserDAO;

    String authValid = "test1*1";

    @BeforeEach
    public void setUp() {
        mockTransactionDAO = mock(TransactionDAO.class);
        transactionController = new TransactionController(mockTransactionDAO);
        mockUser = mock(UserAccount.class);
        mockUserDAO = mock(UserFileDAO.class);
        
    }
  
   @Test
    public void testCreateTransaction() throws IOException, IllegalArgumentException, AccountNotFoundException, InvalidTokenException {
        Integer expected_id = 1;
        Integer expected_user = 2;
        String expected_dateTime = "3/27";
        String expected_paymentMethod = "visa";
        String expected_shippingAddress = "address";
        TransactionInfo transactionInfo = new TransactionInfo(expected_paymentMethod, expected_shippingAddress);
        CartProduct[] expected_products = {new CartProduct(1, 1), new CartProduct(4, 4)};


        Transaction transaction = new Transaction(expected_id, expected_user, expected_products, expected_dateTime, expected_paymentMethod, expected_shippingAddress);

        when(mockTransactionDAO.createTransaction(authValid, expected_paymentMethod, expected_shippingAddress)).thenReturn(transaction);
        
        ResponseEntity<Transaction> response = transactionController.createTransaction(authValid, transactionInfo);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody(), transaction);
    }

    @Test
    public void testCreateTransactionEmptyName() throws IOException, IllegalArgumentException, AccountNotFoundException, InvalidTokenException{
        String expected_paymentMethod = "";
        String expected_shippingAddress = "address";

        when(mockTransactionDAO.createTransaction(authValid, expected_paymentMethod, expected_shippingAddress)).thenReturn(null);

        ResponseEntity<Transaction> response = transactionController.createTransaction(authValid, new TransactionInfo(expected_paymentMethod, expected_shippingAddress));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testError() throws IOException, IllegalArgumentException, AccountNotFoundException, InvalidTokenException{
        String expected_paymentMethod = "visa";
        String expected_shippingAddress = "address";
        TransactionInfo transactionInfo = new TransactionInfo(expected_paymentMethod, expected_shippingAddress);

        when(mockTransactionDAO.createTransaction(authValid, expected_paymentMethod, expected_shippingAddress)).thenThrow(new IOException());

        ResponseEntity<Transaction> response = transactionController.createTransaction(authValid, transactionInfo);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
  
    @Test
    public void testGetTransaction()
    {
        Integer expected_id = 1;
        Integer expected_user = 2;
        String expected_dateTime = "3/27";
        String expected_paymentMethod = "visa";
        String expected_shippingAddress = "address";
        CartProduct[] expected_products = {new CartProduct(1, 1), new CartProduct(4, 4)};
        
        Transaction transaction = new Transaction(expected_id, expected_user, expected_products, expected_dateTime, expected_paymentMethod, expected_shippingAddress);

        when(mockTransactionDAO.getTransaction(transaction.getId())).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransaction(expected_id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetTransactionNotFound()
    {
        Integer expected_id = 1;
        Integer unexpected_id = 2;
        Integer expected_user = 2;
        String expected_dateTime = "3/27";
        String expected_paymentMethod = "visa";
        String expected_shippingAddress = "address";
        CartProduct[] expected_products = {new CartProduct(1, 1), new CartProduct(4, 4)};
        
        Transaction transaction = new Transaction(expected_id, expected_user, expected_products, expected_dateTime, expected_paymentMethod, expected_shippingAddress);
        when(mockTransactionDAO.getTransaction(transaction.getId())).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransaction(unexpected_id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllTransactions()
    {
        Integer expected_id = 1;
        Integer expected_user = 2;
        String expected_dateTime = "3/27";
        String expected_paymentMethod = "visa";
        String expected_shippingAddress = "address";
        CartProduct[] expected_products = {new CartProduct(1, 1), new CartProduct(4, 4)};

        Transaction[] transactions = new Transaction[3];
        transactions[0] = new Transaction(expected_id, expected_user, expected_products, expected_dateTime, expected_paymentMethod, expected_shippingAddress);
        transactions[1] = new Transaction(expected_id + 1, expected_user, expected_products, expected_dateTime, expected_paymentMethod, expected_shippingAddress);
        transactions[2] = new Transaction(expected_id + 1, expected_user, expected_products, expected_dateTime, expected_paymentMethod, expected_shippingAddress);
        
        when(mockTransactionDAO.getAllTransactions()).thenReturn(transactions);

        ResponseEntity<Transaction[]> response = transactionController.getAllTransactions();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), transactions);
    }

    @Test
    public void testChangeFulfilledStatus()
    {
        Integer expected_id = 1;
        Integer expected_user = 2;
        String expected_dateTime = "3/27";
        String expected_paymentMethod = "visa";
        String expected_shippingAddress = "address";
        CartProduct[] expected_products = {new CartProduct(1, 1), new CartProduct(4, 4)};
        
        Transaction transaction = new Transaction(expected_id, expected_user, expected_products, expected_dateTime, expected_paymentMethod, expected_shippingAddress);

        when(mockUser.getIsAdmin()).thenReturn(true);

        ResponseEntity<Transaction> response = transactionController.changeFulfilledStatus(expected_id, true, authValid);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        
    }
    
    @Test
    public void testChangeFulfilledStatusIT() throws AccountNotFoundException, InvalidTokenException
    {
        Integer expected_id = 1;
        Integer expected_user = 2;
        String expected_dateTime = "3/27";
        String expected_paymentMethod = "visa";
        String expected_shippingAddress = "address";
        CartProduct[] expected_products = {new CartProduct(1, 1), new CartProduct(4, 4)};
        
        Transaction transaction = new Transaction(expected_id, expected_user, expected_products, expected_dateTime, expected_paymentMethod, expected_shippingAddress);

        when(mockUserDAO.verifyToken(authValid)).thenThrow(new InvalidTokenException("User with token does not exist."));

        ResponseEntity<Transaction> response = transactionController.changeFulfilledStatus(expected_id, true, authValid);

        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);

    }

}
