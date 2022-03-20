package com.estore.api.estoreapi.persistence;

import java.util.*;
import java.io.*;

import com.estore.api.estoreapi.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartFileDAO implements CartDAO {
    
    private static File cartsDirectory = new File("data/carts");
    private static CartFileDAO instance = null;

    private Map<Integer, Cart> carts;

    /**
     * The object mapper.
     */
    private ObjectMapper objectMapper;    

    private UserDAO userDAO = null;
    private InventoryDAO inventoryDAO = null;
    
    @Autowired
    public CartFileDAO(ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        readAllCarts();
        if(instance == null) instance = this;
    }

    public static CartFileDAO getInstance() {
        return instance;
    }

    private void getDependentInstances() {
        if(this.userDAO == null) this.userDAO = UserFileDAO.getInstance();
        if(this.inventoryDAO == null) this.inventoryDAO = InventoryFileDAO.getInstance();
    }

    @Override
    public Cart createCart(UserAccount user, Cart cart) throws IOException {
        this.carts.put(user.getId(), verifyCart(cart));
        this.writeCart(user.getId(), verifyCart(cart));
        return cart;
    }

    @Override
    public Cart getCart(String token) throws AccountNotFoundException, InvalidTokenException {
        UserAccount user = userDAO.verifyToken(token);
        return verifyCart(carts.get(user.getId()));
    }

    @Override
    public Cart updateCart(String token, Cart cart) throws AccountNotFoundException, InvalidTokenException, IOException {
        int id = userDAO.verifyToken(token).getId();
        carts.put(id, cart);
        writeCart(id, cart);
        return verifyCart(carts.get(id));
    }

    @Override
    public Cart clearCart(String token) throws AccountNotFoundException, InvalidTokenException, IOException {
        int id = userDAO.verifyToken(token).getId();
        Cart old = new Cart(carts.get(id).getProducts());
        carts.put(id, Cart.EMPTY);
        writeCart(id, Cart.EMPTY);
        return old;
    }
    
    public Cart deleteCart(String token) throws AccountNotFoundException, InvalidTokenException, IOException {
        int id = userDAO.verifyToken(token).getId();
        Cart old = new Cart(carts.get(id).getProducts());
        carts.remove(id);
        deleteCartFile(getCartFile(id));
        return old;
    }

    private Cart verifyCart(Cart cart) {
        ArrayList<CartProduct> products = new ArrayList<>();
        for(CartProduct product : cart.getProducts()) {
            try{
                inventoryDAO.getProduct(product.getId());
                products.add(product);
            } catch(IOException ioe) {}
        }
        if(products.size() == cart.getProducts().length) return cart;
        else return new Cart(products.toArray(new CartProduct[0]));
    }

    private void readAllCarts() throws IOException {
        this.carts = new TreeMap<Integer, Cart>();
        for(File cartFile : cartsDirectory.listFiles()) {
            Integer id = Integer.parseInt(cartFile.getName().replaceAll(".json", ""));
            this.carts.put(id, readCart(cartFile));
        }
    }

    private Cart readCart(File cartFile) throws IOException {
        CartProduct[] products = objectMapper.readValue(cartFile, CartProduct[].class);
        return new Cart(products);
    }

    private void writeCart(int id, Cart cart) throws IOException {
        File newCart = new File(cartsDirectory, id + ".json");
        objectMapper.writeValue(newCart, cart.getProducts());
    }

    private File getCartFile(int id) {
        return(new File(cartsDirectory, id + ".json"));
    }

    private void deleteCartFile(File cartFile) {
        cartFile.delete();
    }
}
