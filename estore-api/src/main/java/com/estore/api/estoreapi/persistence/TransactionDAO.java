package com.estore.api.estoreapi.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.estore.api.estoreapi.model.*;

public interface TransactionDAO {

    public Transaction createTransaction(Transaction transaction) throws IOException;

    public Transaction getTransaction(Integer id);

}