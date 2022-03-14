package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.UserAccount;

public interface UserDAO {
    // Needs method headers

    public UserAccount createUser(UserAccount user) throws IOException;

    public UserAccount loginUser(String username);
}
