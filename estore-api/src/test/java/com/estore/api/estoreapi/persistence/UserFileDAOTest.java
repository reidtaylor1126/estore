package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    UserAccount[] testUsers;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setUp() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new UserAccount[3];
        testUsers[0] = new UserAccount();
        testUsers[1] = new UserAccount();
        testUsers[2] = new UserAccount();

        when(mockObjectMapper.readValue(
                new File("filenotfound.txt"), UserAccount[].class)).thenReturn(testUsers);
        userFileDAO = new UserFileDAO("filenotfound.txt",
                mockObjectMapper);
    }
}