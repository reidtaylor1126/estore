package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductFileDAO implements ProductDAO {

    private Map<Integer, Product> products;
    private String filename;
    private ObjectMapper objectMapper;
    private static int nextId;

    public ProductFileDAO(@Value("${product.filename}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadProducts();
    }

    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    private ArrayList<Product> getProductsArray() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Product createProduct(Product product) throws IOException {
        synchronized (products) {
            Product newProduct = new Product(nextId(), product.getName(), product.getDescription(), product.getPrice(),
                    product.getQuantity());
            products.put(newProduct.getId(), newProduct);
            saveProducts();
        }
        return null;
    }

    private void saveProducts() throws IOException {
        objectMapper.writeValue(new File(filename), getProductsArray());
    }

    private void loadProducts() throws IOException {
        products = new TreeMap<>();
        nextId = 0;
        System.out.println(System.getProperty("user.dir"));
        Product[] productArray = objectMapper.readValue(new File(filename), Product[].class);
        for (Product product : productArray) {
            products.put(product.getId(), product);
            if (product.getId() > nextId) {
                nextId = product.getId();
            }
        }
        nextId++;
    }

}
