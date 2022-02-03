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
public class InventoryFileDAO implements InventoryDAO {

    private Map<Integer, Product> inventory;
    private String filename;
    private ObjectMapper objectMapper;
    private static int nextId;

    public InventoryFileDAO(@Value("${inventory.filename}") String filename, ObjectMapper objectMapper)
            throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadInventory();
    }

    private synchronized static int nextId() {
        int id = nextId;
        nextId++;
        return id;
    }

    private ArrayList<Product> getInventoryArray() {
        return new ArrayList<>(inventory.values());
    }

    @Override
    public Product createProduct(Product product) throws IOException, IllegalArgumentException {
        synchronized (inventory) {
            Product newProduct = new Product(nextId(), product.getName(), product.getDescription(), product.getPrice(),
                    product.getQuantity());

            if (inventory.values().stream().anyMatch(p -> p.getName().equals(newProduct.getName()))) {
                throw new IllegalArgumentException("Product with name " + newProduct.getName() + " already exists");
            }
            inventory.put(newProduct.getId(), newProduct);
            saveInventory();
            return newProduct;
        }
    }

    @Override
    public Product getProduct(int id) {
        synchronized (inventory) {
            return inventory.get(id);
        }
    }

    private void saveInventory() throws IOException {
        objectMapper.writeValue(new File(filename), getInventoryArray());
    }

    private void loadInventory() throws IOException {
        inventory = new TreeMap<>();
        nextId = 0;
        Product[] inventoryArray = objectMapper.readValue(new File(filename), Product[].class);
        for (Product product : inventoryArray) {
            inventory.put(product.getId(), product);
            if (product.getId() > nextId) {
                nextId = product.getId();
            }
        }
        nextId++;
    }

}
