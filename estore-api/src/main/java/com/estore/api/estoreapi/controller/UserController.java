package com.estore.api.estoreapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import com.estore.api.estoreapi.model.UserAccount;
import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.persistence.UserDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @GetMapping("/user")
    public ResponseEntity<UserAccount> loginUser(@RequestParam String q) {
        LOG.info("GET /users/user?q=" + q);
        UserAccount user = userDAO.loginUser(q, "");
        if (user != null) {
            return new ResponseEntity<UserAccount>(user, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}