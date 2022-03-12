package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.*;
import java.io.*;

public interface CartDAO {
    public Cart getCart(String token) throws AccountNotFoundException, InvalidTokenException ;

    public Cart updateCart(String token, Cart cart) throws AccountNotFoundException, InvalidTokenException, IOException ;

    public Cart clearCart(String token) throws AccountNotFoundException, InvalidTokenException, IOException ;
}
