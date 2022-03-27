/**
 * @author Nathan (Nate) Appleby npa1508
 */

package com.estore.api.estoreapi.model;

import java.io.ObjectInputStream.GetField;
import java.util.Arrays;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {
    @NotNull
    @JsonProperty
    private Cart transactionCart;

    @NotNull
    @JsonProperty
    private UserAccount transactionAccount;

    @NotNull
    @JsonProperty
    private Integer id;

    public Transaction(@NotNull @JsonProperty("id") int id, 
                        @NotNull @JsonProperty("cart") Cart transactionCart,
                        @NotNull @JsonProperty("account") UserAccount transactionAccount){
        this.id = id;
        this.transactionCart = transactionCart;
        this.transactionAccount = transactionAccount;
    }

    public int getID()
    {
        return id;
    }

    public Cart getCart()
    {
        return transactionCart;
    }

    public UserAccount getAccount()
    {
        return transactionAccount;
    }
    
}
