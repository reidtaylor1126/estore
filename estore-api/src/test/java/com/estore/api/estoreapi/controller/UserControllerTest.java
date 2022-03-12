package test.java.com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}