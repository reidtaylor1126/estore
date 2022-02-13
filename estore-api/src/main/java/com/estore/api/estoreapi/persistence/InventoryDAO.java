package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;

public interface InventoryDAO {
    /**
     * @param product
     * @return the product that was created
     * @throws IOException
     */
    public Product createProduct(Product product) throws IOException;

    public Product[] searchProducts(String searchTerms) throws IOException;

    public Product getProduct(String name);

    public Product updateProduct(Product product) throws IOException;

    public Product[] getInventory() throws IOException;

    public boolean deleteProduct(String name) throws IOException;
}
