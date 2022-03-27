/**
 * @author Nathan (Nate) Appleby npa1508
 */

package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.*;
import java.io.*;

public interface TransactionDAO {
    
    public Transaction getTransaction(int id) throws IOException;
    
}
