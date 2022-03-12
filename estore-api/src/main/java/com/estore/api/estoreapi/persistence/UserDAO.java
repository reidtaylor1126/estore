package com.estore.api.estoreapi.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.estore.api.estoreapi.model.*;

public interface UserDAO {

    public UserAccount loginUser(String username, String password);

    public UserAccount verifyToken(String token) throws FileNotFoundException ;

    public Cart getCart(String token) throws IOException ;

    public Cart updateCart(String token, Cart cart) throws IOException ;

    public Cart clearCart(String token) throws IOException ;
}
