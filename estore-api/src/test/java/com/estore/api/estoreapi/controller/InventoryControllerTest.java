package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.web.multipart.MultipartFile;

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
        Product product2 = new Product(1, null, "testdes", 1.0, 1);

        when(mockInventoryDAO.createProduct(product)).thenReturn(product);

        ResponseEntity<Product> response = inventoryController.createProduct(product);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody(), product);
        when(mockInventoryDAO.createProduct(product2)).thenReturn(null);
        ResponseEntity<Product> response2 = inventoryController.createProduct(product2);
        assertEquals(response2.getStatusCode(), HttpStatus.BAD_REQUEST);

    }

    @Test
    public void testUpdateProduct() throws IOException {
        Product product = new Product(1, "test", "testdes", 1.0, 1);
        Product product2 = new Product(null, "test", "testdes", 1.0, 1);

        when(mockInventoryDAO.updateProduct(product)).thenReturn(product);

        ResponseEntity<Product> response = inventoryController.updateProduct(product);
        when(mockInventoryDAO.updateProduct(product)).thenThrow(new IllegalArgumentException());
        ResponseEntity<Product> response2 = inventoryController.updateProduct(product);
        assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), product);

        when(mockInventoryDAO.updateProduct(product2)).thenReturn(null);
        ResponseEntity<Product> response3 = inventoryController.updateProduct(product2);
        assertEquals(response3.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetInventory() throws IOException {
        Product[] products = new Product[3];
        products[0] = new Product(1, "test", "testdes", 1.0, 1);
        products[1] = new Product(2, "test1", "test2des", 1.0, 1);
        products[2] = new Product(3, "test2", "test3des", 1.0, 1);

        when(mockInventoryDAO.getInventory()).thenReturn(products);

        ResponseEntity<Product[]> response = inventoryController.getInventory();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), products);
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
        when(mockInventoryDAO.updateProduct(product)).thenThrow(new IOException());
        when(mockInventoryDAO.getInventory()).thenThrow(new IOException());

        ResponseEntity<Product> response = inventoryController.createProduct(product);
        ResponseEntity<Product> response2 = inventoryController.updateProduct(product);
        ResponseEntity<Product[]> response3 = inventoryController.getInventory();

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response2.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response3.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDeleteProduct() throws IOException {
        int productId = 1;

        when(mockInventoryDAO.deleteProduct(productId)).thenReturn(true);

        ResponseEntity<Product> response = inventoryController.deleteProduct(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteProductNotFound() throws IOException {
        int productId = 1;

        when(mockInventoryDAO.deleteProduct(productId)).thenReturn(false);

        ResponseEntity<Product> response = inventoryController.deleteProduct(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteProductHandleException() throws IOException {
        int productId = 1;

        when(mockInventoryDAO.deleteProduct(productId)).thenThrow(new IOException());

        ResponseEntity<Product> response = inventoryController.deleteProduct(productId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetProduct() throws IOException {
        Product product1 = new Product(1, "test", "description", 1.0, 1);
        Product product2 = new Product(2, "test2", "description", 1.0, 1);
        Product product3 = new Product(3, "test3", "description", 1.0, 1);

        when(mockInventoryDAO.getProduct(product1.getId())).thenReturn(product1);

        ResponseEntity<Product> response1 = inventoryController.getProduct(product1.getId());
        when(mockInventoryDAO.getProduct(product2.getId())).thenReturn(null);
        ResponseEntity<Product> response2 = inventoryController.getProduct(product2.getId());
        when(mockInventoryDAO.getProduct(product3.getId()))
                .thenThrow(new IllegalArgumentException());
        ResponseEntity<Product> response3 = inventoryController.getProduct(product3.getId());

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(product1, response1.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals(HttpStatus.CONFLICT, response3.getStatusCode());
    }

    @Test
    public void testSearchProduct() throws IOException {
        String searchTerm = "1";
        Product[] products = {new Product(1, "test1", "test desc 1", 10.0, 1),
                new Product(10, "test10", "test desc 10", 7.0, 6),};

        when(mockInventoryDAO.searchProducts(searchTerm)).thenReturn(products);

        ResponseEntity<Product[]> response = inventoryController.searchProduct(searchTerm);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    public void testSearchProductHandleException() throws IOException {
        String searchTerm = "test";

        doThrow(new IOException()).when(mockInventoryDAO).searchProducts(searchTerm);

        ResponseEntity<Product[]> response = inventoryController.searchProduct(searchTerm);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
