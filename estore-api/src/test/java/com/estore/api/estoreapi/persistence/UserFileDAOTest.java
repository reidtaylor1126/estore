package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

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
    CartFileDAO mockCartFileDAO;

    @BeforeEach
    public void setUp() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        mockCartFileDAO = mock(CartFileDAO.class);
        testUsers = new UserAccount[3];
        testUsers[0] = new UserAccount(1, "1");
        testUsers[1] = new UserAccount(2, "2");
        testUsers[2] = new UserAccount(3, "3");

        when(mockObjectMapper.readValue(new File("filenotfound.txt"), UserAccount[].class))
                .thenReturn(testUsers);
        userFileDAO = new UserFileDAO("filenotfound.txt", mockObjectMapper, mockCartFileDAO);
    }

    /**
     * Tests loginUser method with valid username
     */
    @Test
    public void testLoginUser() {
        assertEquals(userFileDAO.loginUser(testUsers[0].getUsername()), testUsers[0]);
        assertEquals(userFileDAO.loginUser(testUsers[1].getUsername()), testUsers[1]);
        assertEquals(userFileDAO.loginUser(testUsers[2].getUsername()), testUsers[2]);
    }

    /**
     * Tests loginUser method with invalid username
     */
    @Test
    public void testLoginUserNotFound() {
        assertEquals(userFileDAO.loginUser("test4"), null);
    }

    @Test
    public void testCreateUser() {
        UserAccount userAccount = new UserAccount(99, "test99");
        UserAccount result = assertDoesNotThrow((() -> userFileDAO.createUser(userAccount)), "Unexpected Exception Thrown");
        assertNotNull(result);
        UserAccount actual = userFileDAO.loginUser(result.getUsername());
        assertEquals(actual.getId(), result.getId());
    }

    @Test
    public void testDuplicates() throws IOException{
        UserAccount userAccount = new UserAccount(3, "3");

        userAccount = userFileDAO.createUser(userAccount);
        assertNull(userAccount);
    }
}
