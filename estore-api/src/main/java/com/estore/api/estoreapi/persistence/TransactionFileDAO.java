/**
 * @author Nathan (Nate) Appleby npa1508
 */

package com.estore.api.estoreapi.persistence;

import java.io.*;
import java.net.http.HttpRequest;
import java.util.*;
import java.util.logging.Logger;

import com.estore.api.estoreapi.model.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("transactionDAO")
public class TransactionFileDAO implements TransactionDAO{

    public TransactionFileDAO(@Value("${users.filename}") String filename, ObjectMapper objectMapper)
    {

    }

    public Transaction getTransaction(int id) throws IOException
    {
        return null;
    }
}
