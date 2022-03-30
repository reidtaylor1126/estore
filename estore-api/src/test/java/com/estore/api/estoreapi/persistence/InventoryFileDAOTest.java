package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.estore.api.estoreapi.model.CartProduct;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence")
public class InventoryFileDAOTest {
        InventoryFileDAO inventoryFileDAO;
        Product[] testProducts;
        ObjectMapper mockObjectMapper;

        @BeforeEach
        public void setUp() throws IOException {
                mockObjectMapper = mock(ObjectMapper.class);
                testProducts = new Product[3];
                testProducts[0] = new Product(1, "test", "testdes", 1.0, 1);
                testProducts[1] = new Product(2, "test1", "test2des", 1.0, 1);
                testProducts[2] = new Product(3, "test2", "test3des", 1.0, 1);

                when(mockObjectMapper.readValue(new File("filenotfound.txt"), Product[].class))
                                .thenReturn(testProducts);
                inventoryFileDAO = new InventoryFileDAO("filenotfound.txt", mockObjectMapper);
        }

        @Test
        public void testCreateProduct() {
                Product product = new Product(234, "test234", "test2314", 1.0, 1);

                Product result = assertDoesNotThrow((() -> inventoryFileDAO.createProduct(product)),
                                "Unexpected exception thrown");
                assertNotNull(result);
                Product actual = inventoryFileDAO.getProduct(result.getId());
                assertEquals(actual.getName(), product.getName());
                assertEquals(actual.getName(), product.getName());
        }

        @Test
        public void testDuplicates() throws IOException {
                Product product = new Product(4, "test4", "testdes", 1.0, 1);
                Product product2 = new Product(45, "test4dasfsdfasda", "testdes", 1.0, 1);

                inventoryFileDAO.createProduct(product);
                assertThrows(IllegalArgumentException.class,
                                (() -> inventoryFileDAO.createProduct(product)),
                                "Unexpected exception thrown");

                assertThrows(IllegalArgumentException.class,
                                (() -> inventoryFileDAO.updateProduct(product2)),
                                "Unexpected exception thrown");
        }

        @Test
        public void testUpdateProduct() {
                Product product = new Product(1, "test", "testdes", 1.0, 1);

                Product result = assertDoesNotThrow((() -> inventoryFileDAO.updateProduct(product)),
                                "Unexpected exception thrown");
                assertNotNull(result);
                Product actual = inventoryFileDAO.getProduct(product.getId());
                assertEquals(actual.getName(), product.getName());
                assertEquals(actual.getName(), product.getName());
        }

        @Test
        public void testGetInventory() {
                Product[] result = assertDoesNotThrow((() -> inventoryFileDAO.getInventory()),
                                "Unexpected exception thrown");
                assertNotNull(result);
                assertEquals(result.length, testProducts.length);
        }

        @Test
        public void testDeleteProduct() throws IOException {
                boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteProduct(1),
                                "Unexpected exception thrown");

                assertEquals(result, true);

                assertEquals(inventoryFileDAO.getInventory().length, testProducts.length - 1);

        }

        @Test
        public void testDeleteProductNotFound() {
                boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteProduct(450),
                                "Unexpected exception thrown");

                assertEquals(result, false);
                assertEquals(inventoryFileDAO.getInventory().length, testProducts.length);
        }

        @Test
        public void testGetProduct() {
                Product product1 = inventoryFileDAO.getProduct(1);
                Product product2 = inventoryFileDAO.getProduct(3);
                Product product3 = inventoryFileDAO.getProduct(560);

                assertEquals(product1, testProducts[0]);
                assertEquals(product2, testProducts[2]);
                assertEquals(product3, null);
        }

        @Test
        public void testSearchProduct() throws IOException {
                String searchTerms = "1";

                Product[] products = inventoryFileDAO.searchProducts(searchTerms);

                assertEquals(testProducts[1], products[0]);
        }

        @Test
        public void testConfirmTransaction()
        {
                Product[] products = new Product[3];
                products[0] = new Product(1, "Product1", "Product1", 10.99, 1);
                products[1] = new Product(2, "Product2", "Product2", 11.99, 2);
                products[2] = new Product(3, "Product3", "Product3", 12.99, 3);
                Transaction test = new Transaction(99, 99, products, "testDate", "testPayment", "testAddress");

                assertTrue(inventoryFileDAO.confirmTransaction(test));
        }
}
