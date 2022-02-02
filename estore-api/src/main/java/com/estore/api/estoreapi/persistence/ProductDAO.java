package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;

public interface ProductDAO {
    /**
     * @param product
     * @return the product that was created
     * @throws IOException
     */
    public Product createProduct(Product product) throws IOException;
}
