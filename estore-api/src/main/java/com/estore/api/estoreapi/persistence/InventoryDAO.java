package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;

/**
 * @author Ryan Yocum
 * @author Nate Appleby
 * @author Reid Taylor
 * @author Clay Rankin
 * 
 *         The InventoryDAO interface represents the persistence layer for the inventory.
 */
public interface InventoryDAO {
    /**
     * @param product
     * @return the product that was created
     * @throws IOException if cannot create product
     */
    public Product createProduct(Product product) throws IOException;

    /**
     * @param searchTerms
     * @return the products that match the search terms
     * @throws IOException if cannot search products
     */
    public Product[] searchProducts(String searchTerms) throws IOException;

    /**
     * @param id
     * @return the product with the given id
     * @throws IOException if cannot get product
     */
    public Product getProduct(Integer id) throws IOException;

    /**
     * @param product
     * @return the product that was updated
     * @throws IOException if cannot update product
     */
    public Product updateProduct(Product product) throws IOException;

    /**
     * @return the inventory
     * @throws IOException if cannot get inventory
     */
    public Product[] getInventory() throws IOException;

    /**
     * @param id
     * @return if the delete was successful
     * @throws IOException if cannot delete the product
     */
    public boolean deleteProduct(Integer id) throws IOException;
}
