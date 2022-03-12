package com.estore.api.estoreapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.persistence.UserDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/cart")
public class CartController {
    private static final Logger LOG = Logger.getLogger(CartController.class.getName());
    private UserDAO cartDAO;

    public CartController(UserDAO cartDAO) {
        this.cartDAO = cartDAO;
    }
    
    @GetMapping("")
    public ResponseEntity<Cart> getCart(@RequestHeader("token") String token) {
        LOG.info("GET /cart");
        try {
            Cart cart = cartDAO.getCart(token);
            System.out.printf("Controller received cart: '%s'\n", cart);
            if(cart == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            else return new ResponseEntity<Cart>(cart, HttpStatus.OK);
        } catch(IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Cart> updateCart(@Valid @RequestHeader("token") String token, @Valid @RequestBody Cart cart) {
        LOG.info("PUT /cart " + token + " " + cart);
        try {
            Cart newCart = cartDAO.updateCart(token, cart);
            if(newCart == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            else return new ResponseEntity<Cart>(newCart, HttpStatus.OK);
        } catch(IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Cart> clearCart(@Valid @RequestHeader("token") String token) {
        LOG.info("Delete /cart " + token);
        try {
            Cart newCart = cartDAO.clearCart(token);
            if(newCart == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            else return new ResponseEntity<Cart>(newCart, HttpStatus.OK);
        } catch(IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
