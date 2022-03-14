package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.beans.Transient;
import java.io.IOException;

import com.estore.api.estoreapi.model.UserAccount;
import com.estore.api.estoreapi.persistence.UserDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("Controller")
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;

    @BeforeEach
    public void setUp() {
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);
    }

    @Test
    public void testLoginUser() {
        String username = "test";
        int userId = 12345;
        boolean admin = false;
        UserAccount user = new UserAccount(1, "1");
        user.setAdmin(admin);
        user.setUsername(username);
        user.setId(userId);

        when(mockUserDAO.loginUser(user.getUsername())).thenReturn(user);

        ResponseEntity<String> response = userController.loginUser(username);

        String sessionKey = username + "*" + userId + "*" + admin;

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), sessionKey);
    }

    @Test
    public void testLoginUserNotFound() {
        String username = "test";

        when(mockUserDAO.loginUser(username)).thenReturn(null);

        ResponseEntity<String> response = userController.loginUser(username);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateUser() throws IOException {
        UserAccount userAccount = new UserAccount(99, "99");

        when(mockUserDAO.createUser(userAccount)).thenReturn(userAccount);

        ResponseEntity<UserAccount> response = userController.createUser(userAccount);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody(), userAccount);
    }

    @Test
    public void testCreateUserDuplicate() throws IOException {
        UserAccount userAccount = new UserAccount(99, "99");

        when(mockUserDAO.createUser(userAccount)).thenReturn(null);

        ResponseEntity<UserAccount> response = userController.createUser(userAccount);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserEmptyName() throws IOException{
        UserAccount userAccount = new UserAccount(99, "");

        when(mockUserDAO.createUser(userAccount)).thenReturn(null);

        ResponseEntity<UserAccount> response = userController.createUser(userAccount);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testError() throws IOException{
        UserAccount userAccount = new UserAccount(99, "99");

        when(mockUserDAO.createUser(userAccount)).thenThrow(new IOException());

        ResponseEntity<UserAccount> response = userController.createUser(userAccount);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
