package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.List;


import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Ryan Yocum
 * @author Nate Appleby
 * @author Reid Taylor
 * @author Clay Rankin
 * 
 *         The InventoryFileDAO class represents the persistence layer for the inventory.
 */
@Component
public class InventoryFileDAO implements InventoryDAO {

    private static InventoryFileDAO instance;

    /**
     * The current inventory.
     */
    private Map<Integer, Product> inventory;

    /**
     * The file name of the inventory file.
     */
    private String filename;

    /**
     * The object mapper.
     */

    private ObjectMapper objectMapper;

    /**
     * The next id to assign to a product.
     */
    private int nextId;

    /**
     * Constructor.
     *
     * @param filename the file name of the inventory file
     */

    public InventoryFileDAO(@Value("${inventory.filename}") String filename,
            ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadInventory();
        if(instance == null) instance = this;
    }

    public static InventoryFileDAO getInstance() {
        return instance;
    }

    /**
     * Gets the next id to assign to a product.
     * 
     * @return the next id to assign to a product
     */
    private synchronized int nextId() {
        return nextId++;
    }

    /**
     * Gets the inventory.
     */

    private ArrayList<Product> getInventoryArray() {
        return new ArrayList<>(inventory.values());
    }

    /**
     * Creates a new product.
     */
    @Override
    public Product createProduct(Product product) throws IOException, IllegalArgumentException {
        synchronized (inventory) {
            Product newProduct = new Product(nextId(), product.getName(), product.getDescription(),
                    product.getPrice(), product.getQuantity());


            // Checks if the name is unique
            if (!checkDuplicateName(newProduct.getName())) {
                throw new IllegalArgumentException("Product name must be unique");
            }

            inventory.put(newProduct.getId(), newProduct);
            saveInventory();
            return newProduct;
        }
    }

    /**
     * Searches for a product by name.
     */
    @Override
    public Product[] searchProducts(String searchTerms) {
        if (searchTerms.length() == 0)
            return new Product[0];

        ArrayList<Product> products = new ArrayList<>();
        for (Product product : inventory.values()) {
            if (product.getName().toLowerCase().contains(searchTerms.toLowerCase())) {
                products.add(product);
            }
        }

        return products.toArray(new Product[0]);
    }

    /**
     * Gets the entire inventory.
     */
    public Product[] getInventory() {
        return getInventoryArray().toArray(new Product[0]);
    }


    /**
     * Gets a product by id.
     */
    @Override
    public Product getProduct(Integer id) {
        synchronized (inventory) {
            Product tempProduct = inventory.get(id);
            return tempProduct;
        }
    }

    /**
     * Updates a product.
     * 
     * @return the updated product
     */
    @Override
    public Product updateProduct(Product product) throws IOException, IllegalArgumentException {
        synchronized (inventory) {
            if (!inventory.containsKey(product.getId())) {
                throw new IllegalArgumentException(
                        "Product with name " + product.getName() + " does not exist");
            }

            // Grab the copy in the inventory
            Product tempProduct = inventory.get(product.getId());

            // Make sure there are no duplicate names
            String name = product.getName();
            if (!checkDuplicateName(name)) {
                name = tempProduct.getName();
            }

            // Update the product
            Product updatedProduct = new Product(product.getId(), name,
                    product.getDescription() != null ? product.getDescription()
                            : tempProduct.getDescription(),
                    product.getPrice() != null ? product.getPrice() : tempProduct.getPrice(),
                    product.getQuantity() != null ? product.getQuantity()
                            : tempProduct.getQuantity());

            // Update the inventory
            inventory.put(updatedProduct.getId(), updatedProduct);
            saveInventory();
            return updatedProduct;
        }
    }

    /**
     * Save the inventory to the file.
     */
    private void saveInventory() throws IOException {
        objectMapper.writeValue(new File(filename), getInventoryArray());
    }

    /**
     * Load the inventory from the file.
     */
    private void loadInventory() throws IOException {
        inventory = new TreeMap<>();
        Product[] inventoryArray = objectMapper.readValue(new File(filename), Product[].class);
        for (Product product : inventoryArray) {
            inventory.put(product.getId(), product);
            if (product.getId() > nextId()) {
                nextId = product.getId();
            }
            ++nextId;
        }
    }

    /**
     * Deletes a product.
     * 
     * @return true if the product was deleted, false otherwise
     */
    @Override
    public boolean deleteProduct(Integer id) throws IOException {

        synchronized (inventory) {
            if (inventory.containsKey(id)) {
                inventory.remove(id);
                saveInventory();
            } else {
                return false;
            }

            return true;
        }
    }

    /**
     * Checks if the name is unique.
     * 
     * @return true if the name is unique, false otherwise
     */
    private boolean checkDuplicateName(String name) {
        List<Product> inventory = getInventoryArray();
        Stream<Product> inventoryStream = inventory.stream();
        return !inventoryStream.anyMatch(p -> p.getName().equals(name));
    }
}
