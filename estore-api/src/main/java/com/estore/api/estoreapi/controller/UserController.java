package com.estore.api.estoreapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import com.estore.api.estoreapi.model.UserAccount;
import com.estore.api.estoreapi.persistence.UserDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO userDAO;

    public UserController(UserDAO _userDAO) {
        this.userDAO = _userDAO;
    }

    /**
     * Creates a new user and adds it to the users.json file
     * 
     * @param userAccount a new userAccount to add
     * @return a ResponseEntity with the HttpStatus of the request
     */
    @PostMapping("")
    public ResponseEntity<UserAccount> createUser(@RequestBody UserAccount userAccount) {
        LOG.info("POST /users " + userAccount);
        try {
            if (userAccount.getUsername() == null || userAccount.getUsername().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                UserAccount newUser = userDAO.createUser(userAccount);
                if (newUser == null) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
                return new ResponseEntity<UserAccount>(newUser, HttpStatus.CREATED);
            }
        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * loginUser - logs user in and generates a sessionKey
     * 
     * @param q - username to log user in
     * 
     * @return ResponseEntity<String> - returns sessionKey
     */
    @GetMapping(value = "", params = "q")
    public ResponseEntity<String> loginUser(@RequestParam String q) {
        LOG.info("GET /users?q=" + q);

        String sessionKey;

        UserAccount user = userDAO.loginUser(q);

        if (user != null) {
            sessionKey = user.getUsername() + "*" + user.getId() + "*" + user.getIsAdmin();

            return new ResponseEntity<String>(sessionKey, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
