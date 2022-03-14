package com.estore.api.estoreapi.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.estore.api.estoreapi.model.*;

public interface UserDAO {

    public UserAccount loginUser(String username, String password);

    /**
     * Determines whether a token matches with an existing user account
     * @param token The submitted token
     * @return A {@link UserAccount} if one matching the token is found
     * @throws AccountNotFoundException If a {@link UserAccount} is not found
     * @throws InvalidTokenException If a {@link UserAccount} is found but the token is invalid
     */
    public UserAccount verifyToken(String token) throws AccountNotFoundException, InvalidTokenException;
}
