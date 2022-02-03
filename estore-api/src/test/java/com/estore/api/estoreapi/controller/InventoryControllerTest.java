package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.InventoryDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("Controller")
public class InventoryControllerTest {
    private InventoryController inventoryController;
    private InventoryDAO mockInventoryDAO;

    @BeforeEach
    public void setUp() {
        mockInventoryDAO = mock(InventoryDAO.class);
        inventoryController = new InventoryController(mockInventoryDAO);
    }

    @Test
    public void testCreateProduct() throws IOException {
        Product product = new Product(1, "test", "testdes", 1.0, 1);

        when(mockInventoryDAO.createProduct(product)).thenReturn(product);

        ResponseEntity<Product> response = inventoryController.createProduct(product);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody(), product);
    }

    @Test
    public void testDuplicates() throws IOException {
        Product product = new Product(1, "test", "testdes", 1.0, 1);

        when(mockInventoryDAO.createProduct(product)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Product> response = inventoryController.createProduct(product);

        assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    public void testError() throws IOException {
        Product product = new Product(1, "test", "testdes", 1.0, 1);

        when(mockInventoryDAO.createProduct(product)).thenThrow(new IOException());

        ResponseEntity<Product> response = inventoryController.createProduct(product);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testEmptyName() throws IOException {
        Product product = new Product(1, null, "testdes", 1.0, 1);

        when(mockInventoryDAO.createProduct(product)).thenReturn(product);

        ResponseEntity<Product> response = inventoryController.createProduct(product);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
