package com.estore.api.estoreapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.estore.api.estoreapi.model.AccountNotFoundException;
import com.estore.api.estoreapi.model.InvalidTokenException;
import com.estore.api.estoreapi.model.Transaction;
import com.estore.api.estoreapi.persistence.TransactionDAO;

import org.apache.commons.logging.Log;
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

/**
 * @author Ryan Yocum
 * @author Nate Appleby
 * @author Reid Taylor
 * @author Clay Rankin
 * 
 *         Controller for the Inventory API
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private static final Logger LOG = Logger.getLogger(TransactionController.class.getName());
    private TransactionDAO transactionDAO;

    /**
     * Constructor.
     *
     * @param TransactionDAO the transaction DAO
     */
    public TransactionController(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    /**
     * Creates a new transaction.
     * 
     * @param transaction the product to create
     * @return the created product
     */
    @PostMapping("")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction, @RequestHeader("token") String token) {
        LOG.info("POST /transactions " + transaction);
        if (transaction.getUser() == null || transaction.getProducts() == null || transaction.getPaymentMethod() == null || transaction.getPaymentMethod().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Transaction newTransaction = transactionDAO.createTransaction(transaction, token);
            if (newTransaction != null)
                return new ResponseEntity<Transaction>(newTransaction, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (AccountNotFoundException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidTokenException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("")
    public ResponseEntity<Transaction> getTransaction(@RequestParam Integer id)
    {
        LOG.info("GET /transactions " + id);
        Transaction transaction = transactionDAO.getTransaction(id);            
        if (transaction != null) 
        {
            return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }

    @GetMapping("")
    public ResponseEntity<Transaction[]> getAllTransactions()
    {
        LOG.info("GET /transactions");
        Transaction[] transactions = transactionDAO.getAllTransactions();
        return new ResponseEntity<Transaction[]>(transactions, HttpStatus.OK);
    }

}