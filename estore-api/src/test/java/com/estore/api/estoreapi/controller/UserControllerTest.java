package test.java.com.estore.api.estoreapi.controller;

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
        // UserAccount user = new UserAccount();

        // when(mockUserDAO.loginUser(user.getUsername()).thenReturn(user));

        // ResponseEntity<String> response = userController.loginUser(user.getUsername());

        // String sessionKey = user.getUsername() + "*" + user.getId();

        // assertEquals(response.getStatusCode(), HttpStatus.OK);
        // assertEquals(response.getBody(), sessionKey);
    }

    @Test 
    public void testLoginUserNotFound() {
        // UserAccount user = new UserAccount();

        // when(mockUserDAO.loginUser(user.getUsername()).thenReturn(null));

        // ResponseEntity<String> response = userController.loginUser(user.getUsername());

        // assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}