/**
 * @author Nathan (Nate) Appleby npa1508
 */

package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model")
public class TransactionTest {
    @Test
    public void testCreate()
    {
        Integer expected_id = 99;
        Cart expected_cart = new Cart();
        UserAccount expected_account = new UserAccount(99, "testUser");

        Transaction transaction = new Transaction(expected_id, expected_cart, expected_account);

        assert transaction.getID() == expected_id;
        assert transaction.getCart() == expected_cart;
        assert transaction.getAccount() == expected_account;
    }

    
}
