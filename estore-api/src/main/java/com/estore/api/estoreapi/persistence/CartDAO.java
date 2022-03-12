package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.model.UserAccount;

public interface CartDAO {

    /**
     * Create a new cart file for the given user and initialize it to the given cart
     * @param user The user for whom the new cart is to be registered
     * @param cart The user's cart
     * @return The newly created cart
     * @throws IOException
     */
    public Cart createCart(UserAccount user, Cart cart) throws IOException ;

    /**
     * Retrieve a user's cart, given that their token is valid
     * @param token The user's authenication token
     * @return the user's cart, or null if the token is invalid
     * @throws IOException
     */
    public Cart getCart(String token) throws IOException ;

    /**
     * Replaces a user's cart data, given that their token is valid
     * @param token The user's authenication token
     * @param cart The new cart data, replacing the current cart data
     * @return the new cart after updating, or null if the token is invalid
     * @throws IOException
     */
    public Cart updateCart(String token, Cart cart) throws IOException ;

    /**
     * Empties a user's cart of data without deleting the file, given that their token is valid
     * @param token The user's authentication token
     * @return the previous contents of the cart, or null if the token is invalid
     * @throws IOException
     */
    public Cart clearCart(String token) throws IOException ;

    /**
     * Thouroughly deletes a user's cart, given that their token is valid<br>
     * Note: In general, {@link deleteCart} should only be used when deleting a user account. {@link clearCart} is prefereable in most scenarios
     * @param token The user's authentication token
     * @return the previous contents of the cart, or null if the token is invalid
     * @throws IOException
     */
    public Cart deleteCart(String token) throws IOException ;
}
