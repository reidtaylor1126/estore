package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.estore.api.estoreapi.model.*;

public class CartFileDAO implements CartDAO{

    private final static File CARTS_DIRECTORY = new File("data");

    Map<Integer, Cart> carts;

    /**
     * The object mapper.
     */
    private ObjectMapper objectMapper;

    private UserFileDAO userFileDAO;
    
    public CartFileDAO(ObjectMapper objectMapper, UserFileDAO userFileDAO) throws IOException {
        this.objectMapper = objectMapper;
        this.userFileDAO = userFileDAO;
        this.readAllCarts();
    }

    public Cart createCart(UserAccount user, Cart cart) throws IOException {
        this.carts.put(user.getId(), cart);
        this.writeCart(user.getId(), cart);
        return cart;
    }

    public Cart getCart(String token) {
        try {
            UserAccount user = userFileDAO.verifyToken(token);
            return carts.get(user.getId());
        } catch(FileNotFoundException fnfe) {
            System.out.printf("Invalid token: '%s'\n", token);
            return null;
        }
    }

    public Cart updateCart(String token, Cart cart) throws IOException {
        try {
            int id = userFileDAO.verifyToken(token).getId();
            carts.put(id, cart);
            writeCart(id, cart);
            return carts.get(id);
        } catch(FileNotFoundException fnfe) {
            System.out.printf("Invalid token: '%s'\n", token);
            return null;
        }
    }

    public Cart clearCart(String token) throws IOException {
        try {
            int id = userFileDAO.verifyToken(token).getId();
            Cart old = carts.get(id);
            carts.put(id, Cart.EMPTY);
            writeCart(id, Cart.EMPTY);
            return old;
        } catch(FileNotFoundException fnfe) {
            System.out.printf("Invalid token: '%s'\n", token);
            return null;
        }
    }

    public Cart deleteCart(String token) throws IOException {
        try {
            int id = userFileDAO.verifyToken(token).getId();
            Cart old = carts.get(id);
            carts.remove(id);
            deleteCartFile(getCartFile(id));
            return old;
        } catch(FileNotFoundException fnfe) {
            System.out.printf("Invalid token: '%s'\n", token);
            return null;
        }
    }

    private void readAllCarts() throws IOException {
        for(File cartFile : CARTS_DIRECTORY.listFiles()) {
            Integer id = Integer.parseInt(cartFile.getName().split(".")[0]);
            this.carts.put(id, readCart(cartFile));
        }
    }

    private Cart readCart(File cartFile) throws IOException {
        Product[] products = objectMapper.readValue(cartFile, Product[].class);
        return new Cart(products);
    }

    private Cart readCart(int id) throws IOException {
        Product[] products = objectMapper.readValue(getCartFile(id), Product[].class);
        return new Cart(products);
    }

    private void writeCart(int id, Cart cart) throws IOException {
        File newCart = new File(CARTS_DIRECTORY, id + ".json");
        objectMapper.writeValue(newCart, cart.getProducts());
    }

    private File getCartFile(int id) throws IOException {
        return(new File(CARTS_DIRECTORY, id + ".json"));
    }

    private void deleteCartFile(File cartFile) throws IOException {
        cartFile.delete();
    }
}
