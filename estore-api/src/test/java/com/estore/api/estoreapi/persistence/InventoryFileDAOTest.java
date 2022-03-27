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

import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

@Tag("Persistence")
public class InventoryFileDAOTest {
        InventoryFileDAO inventoryFileDAO;
        Product[] testProducts;
        ObjectMapper mockObjectMapper;

        @BeforeEach
        public void setUp() throws IOException {
                mockObjectMapper = mock(ObjectMapper.class);
                testProducts = new Product[3];
                String file = "file";
                testProducts[0] = new Product(1, "test", "testdes", 1.0, 1);
                testProducts[1] = new Product(2, "test1", "test2des", 1.0, 1);
                testProducts[2] = new Product(3, "test2", "test3des", 1.0, 1);

                when(mockObjectMapper.readValue(new File("filenotfound.txt"), Product[].class))
                                .thenReturn(testProducts);
                inventoryFileDAO = new InventoryFileDAO("filenotfound.txt", mockObjectMapper);
        }

        @Test
        public void testCreateProduct() {
                String file = "file";
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
                String file = "file";
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
                String file = "file";
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
        public void testSearchProductNotFound() throws IOException {
                String searchTerms = "asdfasdf";

                Product[] products = inventoryFileDAO.searchProducts(searchTerms);

                assertEquals(0, products.length);
        }

        @Test
        public void testAddProductImage() throws IOException {
                MultipartFile file = mock(MultipartFile.class);
                when(file.getOriginalFilename()).thenReturn("test.jpg");
                when(file.getBytes()).thenReturn(new byte[0]);

                Product product = inventoryFileDAO.getProduct(1);
                assertEquals(product.getNumImages(), 0);

                inventoryFileDAO.addProductImage("1", file);

                product = inventoryFileDAO.getProduct(1);
                assertEquals(product.getNumImages(), 1);
                inventoryFileDAO.deleteProductImage(1, 0);
        }

        @Test
        public void testAddProductImageNotFound() throws IOException {
                MultipartFile file = mock(MultipartFile.class);
                when(file.getOriginalFilename()).thenReturn("test.jpg");
                when(file.getBytes()).thenReturn(new byte[0]);

                assertThrows(IllegalArgumentException.class,
                                () -> inventoryFileDAO.addProductImage("18", file),
                                "Unexpected exception thrown");
                assertThrows(IllegalArgumentException.class,
                                () -> inventoryFileDAO.addProductImage("a", file),
                                "Unexpected exception thrown");
        }

        @Test
        public void testDeleteProductImage() throws IOException {
                MultipartFile file = mock(MultipartFile.class);
                when(file.getOriginalFilename()).thenReturn("test.jpg");
                when(file.getBytes()).thenReturn(new byte[0]);

                Product product = inventoryFileDAO.getProduct(1);
                assertEquals(product.getNumImages(), 0);

                inventoryFileDAO.addProductImage("1", file);

                product = inventoryFileDAO.getProduct(1);
                assertEquals(product.getNumImages(), 1);

                inventoryFileDAO.addProductImage("1", file);

                product = inventoryFileDAO.getProduct(1);
                assertEquals(product.getNumImages(), 2);

                inventoryFileDAO.deleteProductImage(1, 0);
                inventoryFileDAO.deleteProductImage(1, 0);

                product = inventoryFileDAO.getProduct(1);
                assertEquals(product.getNumImages(), 0);
        }

        @Test
        public void testDeleteProductImageNotFound() throws IOException {
                MultipartFile file = mock(MultipartFile.class);
                when(file.getOriginalFilename()).thenReturn("test.jpg");
                when(file.getBytes()).thenReturn(new byte[0]);

                assertThrows(IllegalArgumentException.class,
                                () -> inventoryFileDAO.deleteProductImage(18, 0),
                                "Unexpected exception thrown");
                assertThrows(IllegalArgumentException.class,
                                () -> inventoryFileDAO.deleteProductImage(3, 0),
                                "Unexpected exception thrown");
        }

        @Test
        public void testGetProductImage() throws IOException {
                MultipartFile file = mock(MultipartFile.class);
                when(file.getOriginalFilename()).thenReturn("test.jpg");
                when(file.getBytes()).thenReturn(new byte[0]);

                Product product = inventoryFileDAO.getProduct(1);
                assertEquals(product.getNumImages(), 0);

                inventoryFileDAO.addProductImage("1", file);

                product = inventoryFileDAO.getProduct(1);
                assertEquals(product.getNumImages(), 1);

                byte[] result = inventoryFileDAO.getImage(1, 0);
                assertNotNull(result);

                inventoryFileDAO.deleteProductImage(1, 0);
        }

        @Test
        public void testGetProductImageDefault() throws IOException {

                byte[] result = inventoryFileDAO.getImage(-1, -1);
                assertNotNull(result);

                result = inventoryFileDAO.getImage(1, 0);
                assertNotNull(result);

        }

        @Test
        public void testGetProductImageNullProduct() throws IOException {
                assertThrows(IllegalArgumentException.class, () -> inventoryFileDAO.getImage(18, 0),
                                "Unexpected exception thrown");
        }

        @Test
        public void testGetProductImageNotFound() throws IOException {
                MultipartFile file = mock(MultipartFile.class);
                when(file.getOriginalFilename()).thenReturn("test.jpg");
                when(file.getBytes()).thenReturn(new byte[0]);

                Product product = inventoryFileDAO.getProduct(1);
                assertEquals(product.getNumImages(), 0);

                inventoryFileDAO.addProductImage("1", file);

                product = inventoryFileDAO.getProduct(1);
                assertEquals(product.getNumImages(), 1);

                byte[] result = inventoryFileDAO.getImage(1, 1);
                assertNull(result);

                inventoryFileDAO.deleteProductImage(1, 0);
        }
}
