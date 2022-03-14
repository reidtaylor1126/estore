package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.*;
import java.io.*;

public interface CartDAO {

    /**
     * Creates and registers a new {@link Cart} for the specified {@link User}
     * @param user The User to create the cart for
     * @param cart The cart data to assign to the user
     * @return The user's new cart
     * @throws IOException
     */
    public Cart createCart(UserAccount user, Cart cart) throws IOException ;

    /**
     * Attempts to find a user matching the provided token and retrieve their {@link Cart}
     * @param token The user's authentication token
     * @return The user's {@link Cart}
     * @throws AccountNotFoundException If a {@link UserAccount} is not found
     * @throws InvalidTokenException If a {@link UserAccount} is found but the token is invalid
     */
    public Cart getCart(String token) throws AccountNotFoundException, InvalidTokenException ;

    /**
     * Attempts to find a user matching the provided token and replaces their {@link Cart} with the provided one
     * @param token The user's authentication token
     * @param cart The user's {@link Cart}
     * @return The new {@link Cart}
     * @throws AccountNotFoundException If a {@link UserAccount} is not found
     * @throws InvalidTokenException If a {@link UserAccount} is found but the token is invalid
     * @throws IOException
     */
    public Cart updateCart(String token, Cart cart) throws AccountNotFoundException, InvalidTokenException, IOException ;

    /**
     * Attempts to find a user matching the provided token and clears their {@link Cart}
     * @param token The user's authentication token
     * @return The old {@link Cart}
     * @throws AccountNotFoundException If a {@link UserAccount} is not found
     * @throws InvalidTokenException If a {@link UserAccount} is found but the token is invalid
     * @throws IOException
     */
    public Cart clearCart(String token) throws AccountNotFoundException, InvalidTokenException, IOException ;

    /**
     * Attempts to find a user matching the provided token and unregisters and deletes their {@link Cart}<br>
     * Note: This should only be used when deleting the user
     * @param token The user's authentication token
     * @return The old {@link Cart}
     * @throws AccountNotFoundException If a {@link UserAccount} is not found
     * @throws InvalidTokenException If a {@link UserAccount} is found but the token is invalid
     * @throws IOException
     */
    public Cart deleteCart(String token) throws AccountNotFoundException, InvalidTokenException, IOException ;
}
