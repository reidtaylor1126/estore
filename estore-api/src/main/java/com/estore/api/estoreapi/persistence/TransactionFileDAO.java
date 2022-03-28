package com.estore.api.estoreapi.persistence;

import java.io.*;
import java.net.http.HttpRequest;
import java.util.*;
import java.text.*;
import java.util.logging.Logger;

import com.estore.api.estoreapi.model.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("transactionDAO")
public class TransactionFileDAO implements TransactionDAO {
    private static final Logger LOG = Logger.getLogger(TransactionFileDAO.class.getName());
    private static TransactionFileDAO instance = null;

    /**
     * The set of transactions
     */
    private Map<Integer, Transaction> transactions;

    /**
     * The file name of the transaction data file
     */
    private String filename;

    /**
     * The object mapper.
     */
    private ObjectMapper objectMapper;    

    private static int nextId;

    @Autowired
    public TransactionFileDAO(@Value("${transactions.filename}") String filename, ObjectMapper objectMapper)
            throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadTransactions();
        if(instance == null) instance = this;
    }

    public static TransactionFileDAO getInstance() {
        return instance;
    }

    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    private Transaction[] getTransactionArray() {
        ArrayList<Transaction> transactionArrayList = new ArrayList<>();

        for (Transaction transaction : transactions.values()) {
            transactionArrayList.add(transaction);
        }

        Transaction[] transactionArray = new Transaction[transactionArrayList.size()];
        transactionArrayList.toArray(transactionArray);
        return transactionArray;
    }

    private void saveTransactions() throws IOException {
        Transaction[] transactionArray = getTransactionArray();
        objectMapper.writeValue(new File(filename), transactionArray);
    }

    private void loadTransactions() throws IOException {
        transactions = new TreeMap<>();
        nextId = 0; // May need to be changed to 1
        Transaction[] transactionArray = objectMapper.readValue(new File(filename), Transaction[].class);
        for (Transaction transaction : transactionArray) {
            transactions.put(transaction.getId(), transaction);
            if (transaction.getId() > nextId()) {
                nextId = transaction.getId();
            }
            ++nextId;
        }
    }

    public Transaction createTransaction(Transaction transaction) throws IOException {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dateTime = dateFormat.format(date);
        Integer id = nextId();
        Transaction newTransaction = new Transaction(id, transaction.getUser(), transaction.getProducts(), dateTime, transaction.getPaymentMethod());
        synchronized (transactions) {
            transactions.put(id, newTransaction);
            saveTransactions();
        }
        return newTransaction;
    }

    private boolean confirmTransaction(Transaction transaction) throws IOException
    {
        try
        {
            CartProduct[] cartItems = transaction.getProducts();
            InventoryFileDAO inventory = new InventoryFileDAO("inventory.json", objectMapper);
            for (int i = 0; i < cartItems.length; i++)
            {
                int tempProductID = cartItems[i].getId();
                int tempProductQty = cartItems[i].getQuantity();
                Product tempProduct = inventory.getProduct(tempProductID);
                int oldStock = tempProduct.getQuantity();
                int newStock = oldStock - tempProductQty;
                inventory.updateProduct(new Product(tempProductID, null, null, null, newStock));
            }
            return true;
        } catch(IOException e) 
        {
            return false;
        }

    }
}