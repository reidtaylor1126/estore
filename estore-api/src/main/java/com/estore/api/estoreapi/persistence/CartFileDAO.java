package com.estore.api.estoreapi.persistence;

import java.util.*;
import java.io.*;

import com.estore.api.estoreapi.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartFileDAO implements CartDAO {
    
    private final static File CARTS_DIRECTORY = new File("data/carts");

    Map<Integer, Cart> carts;

    /**
     * The object mapper.
     */
    private ObjectMapper objectMapper;    

    private UserDAO userDAO = null;
    
    @Autowired
    public CartFileDAO(ObjectMapper objectMapper, UserDAO userDAO) throws IOException {
        this.objectMapper = objectMapper;
        this.userDAO = userDAO;
        readAllCarts();
    }

    public Cart createCart(UserAccount user, Cart cart) throws IOException {
        this.carts.put(user.getId(), cart);
        this.writeCart(user.getId(), cart);
        return cart;
    }

    public Cart getCart(String token) throws AccountNotFoundException, InvalidTokenException {
        UserAccount user = userDAO.verifyToken(token);
        return carts.get(user.getId());
    }

    public Cart updateCart(String token, Cart cart) throws AccountNotFoundException, InvalidTokenException, IOException {
        try {
            int id = userDAO.verifyToken(token).getId();
            carts.put(id, cart);
            writeCart(id, cart);
            return carts.get(id);
        } catch(FileNotFoundException fnfe) {
            System.out.printf("Invalid token: '%s'\n", token);
            return null;
        }
    }

    public Cart clearCart(String token) throws AccountNotFoundException, InvalidTokenException, IOException {
        int id = userDAO.verifyToken(token).getId();
        Cart old = carts.get(id);
        carts.put(id, Cart.EMPTY);
        writeCart(id, Cart.EMPTY);
        return old;
    }

    public Cart deleteCart(String token) throws AccountNotFoundException, InvalidTokenException, IOException {
        int id = userDAO.verifyToken(token).getId();
        Cart old = carts.get(id);
        carts.remove(id);
        deleteCartFile(getCartFile(id));
        return old;
    }

    private void readAllCarts() throws IOException {
        this.carts = new TreeMap<Integer, Cart>();
        for(File cartFile : CARTS_DIRECTORY.listFiles()) {
            Integer id = Integer.parseInt(cartFile.getName().replaceAll(".json", ""));
            this.carts.put(id, readCart(cartFile));
        }
    }

    private Cart readCart(File cartFile) throws IOException {
        Product[] products = objectMapper.readValue(cartFile, Product[].class);
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
