package com.estore.api.estoreapi.persistence;

import java.io.*;
import java.net.http.HttpRequest;
import java.util.*;

import com.estore.api.estoreapi.model.UserAccount;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserFileDAO implements UserDAO {

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

    private static int nextId;

    public UserFileDAO(@Value("${users.filename}") String filename, ObjectMapper objectMapper)
            throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadUsers();
    }

    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    private UserAccount[] getUserArray() {
        ArrayList<UserAccount> userArrayList = new ArrayList<>();

        for (UserAccount user : users.values()) {
            userArrayList.add(user);
        }

        UserAccount[] userArray = new UserAccount[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    private void saveUsers() throws IOException {
        UserAccount[] userArray = getUserArray();
        objectMapper.writeValue(new File(filename), userArray);
    }

    private void loadUsers() throws IOException {
        users = new TreeMap<>();
        nextId = 0; // May need to be changed to 1
        UserAccount[] userArray = objectMapper.readValue(new File(filename), UserAccount[].class);
        for (UserAccount account : userArray) {
            users.put(account.getUsername(), account);
            if (account.getId() > nextId()) {
                nextId = account.getId();
            }
            ++nextId;
        }
    }

    /**
     * loginUser - checks if username is in users, returns user if found, if not return null user
     * 
     * @param username - username of account
     * @return UserAccount - logged in user, or null user if none were found.
     */

    public UserAccount loginUser(String username) {
        UserAccount user = null;

        if (users.containsKey(username)) {
            user = users.get(username);

            return user;
        }

        return user;
    }

    // CRUD methods need implementation

    @Override
    public UserAccount createUser(UserAccount user) throws IOException, IllegalArgumentException {
        synchronized (users) {
            UserAccount newUser = new UserAccount(nextId(), user.getUsername());
            if( users.containsKey(newUser.getUsername())){
                return null;
            }
            users.put(newUser.getUsername(), newUser);
            saveUsers();
            return newUser;
        }
    }
}
