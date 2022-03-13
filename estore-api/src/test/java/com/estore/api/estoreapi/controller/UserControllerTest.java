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

import main.java.com.estore.api.estoreapi.controller.UserController;

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
        UserAccount user = new UserAccount();
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
}
