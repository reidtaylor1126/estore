package com.estore.api.estoreapi.persistence;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;

import com.estore.api.estoreapi.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartFileDAO implements CartDAO {
    private static final Logger LOG = Logger.getLogger(CartFileDAO.class.getName());
    private File cartsDirectory = new File("data/carts");
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

    public CartFileDAO(ObjectMapper objectMapper, String cartsDirectory) throws IOException {
        this.objectMapper = objectMapper;
        this.cartsDirectory = new File(cartsDirectory);
        readAllCarts();
        if(instance == null) instance = this;
    }

    public static CartFileDAO getInstance() {
        return instance;
    }

    private void getSingletonDependencies() {
        if(this.userDAO == null) this.userDAO = UserFileDAO.getInstance();
        if(this.inventoryDAO == null) this.inventoryDAO = InventoryFileDAO.getInstance();
    }

    @Override
    public Cart createCart(UserAccount user, Cart cart) throws IOException {
        getSingletonDependencies();
        File newCart = new File(cartsDirectory, user.getId() + ".json");
        if(newCart.createNewFile()) {
            Cart verified = verifyCart(cart);
            this.carts.put(user.getId(), verified);
            this.writeCart(user.getId(), verified);
            return cart;
        } else {
            throw new FileAlreadyExistsException(newCart.getName());
        }
    }

    @Override
    public Cart getCart(String token) throws AccountNotFoundException, InvalidTokenException {
        getSingletonDependencies();
        UserAccount user = userDAO.verifyToken(token);
        return verifyCart(carts.get(user.getId()));
    }

    @Override
    public Cart updateCart(String token, Cart cart) throws AccountNotFoundException, InvalidTokenException, IOException {
        getSingletonDependencies();
        int id = userDAO.verifyToken(token).getId();
        carts.put(id, cart);
        writeCart(id, cart);
        return verifyCart(carts.get(id));
    }

    @Override
    public Cart clearCart(String token) throws AccountNotFoundException, InvalidTokenException, IOException {
        getSingletonDependencies();
        int id = userDAO.verifyToken(token).getId();
        Cart old = new Cart(carts.get(id).getProducts());
        carts.put(id, Cart.EMPTY);
        writeCart(id, Cart.EMPTY);
        return old;
    }
    
    public Cart deleteCart(String token) throws AccountNotFoundException, InvalidTokenException, IOException {
        getSingletonDependencies();
        int id = userDAO.verifyToken(token).getId();
        Cart old = new Cart(carts.get(id).getProducts());
        carts.remove(id);
        deleteCartFile(getCartFile(id));
        return old;
    }

    private Cart verifyCart(Cart cart) {
        getSingletonDependencies();
        ArrayList<CartProduct> products = new ArrayList<>();
        Double totalPrice = 0.0;
        for(CartProduct cartProduct : cart.getProducts()) {
            try{
                Product product = inventoryDAO.getProduct(cartProduct.getId());
                products.add(cartProduct);
                totalPrice += product.getPrice()*cartProduct.getQuantity();
            } catch(IOException ioe) {
                LOG.log(Level.INFO, String.format("An item of ID %d was removed from a cart because it does not exist in the inventory.", cartProduct.getId()));
            }
        }
        Cart verified = products.size() == cart.getProducts().length ? cart : new Cart(products.toArray(new CartProduct[0]));
        verified.setTotalPrice(totalPrice);
        return verified;
    }

    private void readAllCarts() throws IOException {
        this.carts = new TreeMap<Integer, Cart>();
        for(File cartFile : cartsDirectory.listFiles()) {
            if(cartFile.getName().split(".")[1].equals("json")) {
                Integer id = Integer.parseInt(cartFile.getName().replaceAll(".json", ""));
                System.out.printf("Reading cart %s%n",cartFile.getPath());
                this.carts.put(id, readCart(cartFile));
            }
        }
    }

    private Cart readCart(File cartFile) throws IOException {
        CartProduct[] products = objectMapper.readValue(cartFile, CartProduct[].class);
        System.out.printf("Got cart with %d products\n",products.length);
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
