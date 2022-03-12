package com.estore.api.estoreapi.persistence;

import java.io.*;
import java.net.http.HttpRequest;
import java.util.*;

import com.estore.api.estoreapi.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("userDAO")
public class UserFileDAO implements UserDAO{

    /**
     * The set of users
     */
    private Map<String, UserAccount> users;

    /**
     * The file name of the user data file
     */
    private String filename;

    /**
     * The object mapper.
     */
    private ObjectMapper objectMapper;    

    private final static File CARTS_DIRECTORY = new File("${carts.filename}");

    Map<Integer, Cart> carts;

    public UserFileDAO(@Value("${users.filename}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadUsers();
    }

    private ArrayList<UserAccount> getUserList() {
        return new ArrayList<>(users.values());
    }

    private void loadUsers() throws IOException{
        users = new TreeMap<>();
        UserAccount[] userArray = objectMapper.readValue(new File(filename), UserAccount[].class);
        for(UserAccount account : userArray) {
            System.out.printf("Loaded user: '%s'\n", account.getUsername());
            users.put(account.getUsername(), account);
            System.out.println(users.get(account.getUsername()));
        }
    }

    public UserAccount loginUser(String username, String password) {
        UserAccount user = null;

        if (users.containsKey(username)) {
            user = users.get(username);

            return user;
        }

        return user;
    }

    public UserAccount verifyToken(String token) throws FileNotFoundException {
        String[] authFields = token.split(UserAccount.AUTH_SEPARATOR);
        String id = authFields[authFields.length - 1];
        System.out.printf("Got id: '%s'\n", id);
        String username = token.substring(0, token.length()-(id.length()+1));
        System.out.printf("Searching for username: '%s'\n", username);
        System.out.printf("Found? %b\n", users.containsKey(username));
        UserAccount account = users.get(username);
        System.out.println(account);
        if(account == null) throw new FileNotFoundException("User with token '" + token + "' does not exist.");
        else return account;
    }

    public Cart createCart(UserAccount user, Cart cart) throws IOException {
        this.carts.put(user.getId(), cart);
        this.writeCart(user.getId(), cart);
        return cart;
    }

    public Cart getCart(String token) {
        System.out.printf("Getting cart for token '%s'\n", token);
        try {
            System.out.println("Attempting to verify...");
            UserAccount user = verifyToken(token);
            return carts.get(user.getId());
        } catch(FileNotFoundException fnfe) {
            String[] authFields = token.split(UserAccount.AUTH_SEPARATOR);
            String id = authFields[authFields.length - 1];
            String username = token.substring(0, token.length()-(id.length()+1));
            System.out.printf("Invalid token: '%s' for user '%s'\n", token, username);
            return null;
        }
    }

    public Cart updateCart(String token, Cart cart) throws IOException {
        try {
            int id = verifyToken(token).getId();
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
            int id = verifyToken(token).getId();
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
            int id = verifyToken(token).getId();
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
