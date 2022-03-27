package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import com.estore.api.estoreapi.model.AccountNotFoundException;
import com.estore.api.estoreapi.model.InvalidTokenException;
import com.estore.api.estoreapi.model.Transaction;
import com.estore.api.estoreapi.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence")
public class TransactionFileDAOTest {
    TransactionFileDAO transactionFileDAO;
    Transaction[] testTransactions;
    ObjectMapper mockObjectMapper;
    
    @BeforeEach
    public void setUp() throws IOException
    {
        mockObjectMapper = mock(ObjectMapper.class);
        testTransactions =  new Transaction[3];
        testTransactions[0] = new Transaction(99, new Cart(), new UserAccount(99, "test99"));
        testTransactions[1] = new Transaction(98, new Cart(), new UserAccount(98, "test98"));
        testTransactions[2] = new Transaction(97, new Cart(), new UserAccount(97, "test97"));
    }
}
