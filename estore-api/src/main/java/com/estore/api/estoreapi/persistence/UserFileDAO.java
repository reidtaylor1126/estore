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

    private static UserFileDAO instance = null;

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

    public UserFileDAO(@Value("${users.filename}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadUsers();
        if(instance == null) instance = this;
    }

    public static UserFileDAO getInstance() {
        return instance;
    }

    private ArrayList<UserAccount> getUserList() {
        return new ArrayList<>(users.values());
    }

    private void loadUsers() throws IOException{
        users = new TreeMap<>();
        UserAccount[] userArray = objectMapper.readValue(new File(filename), UserAccount[].class);
        for(UserAccount account : userArray) {
            users.put(account.getUsername(), account);
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

    public UserAccount verifyToken(String token) throws AccountNotFoundException, InvalidTokenException {
        String[] authFields = token.split(UserAccount.AUTH_SEPARATOR);
        String id = authFields[authFields.length - 1];
        String username = token.substring(0, token.length()-(id.length()+1));
        UserAccount account = users.get(username);
        if(account == null) throw new AccountNotFoundException("User with token '" + token + "' does not exist.");
        else if(account.getId() != Integer.parseInt(id)) throw new InvalidTokenException("Token '" + token + "' does not match a registered user.");
        else return account;
    }
}
