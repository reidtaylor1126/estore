package com.estore.api.estoreapi.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.estore.api.estoreapi.model.*;

public interface UserDAO {

    public UserAccount loginUser(String username, String password);

    public UserAccount verifyToken(String token) throws AccountNotFoundException, InvalidTokenException;
}
