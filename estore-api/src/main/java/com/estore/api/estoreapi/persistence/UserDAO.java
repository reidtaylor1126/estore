package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.UserAccount;

public interface UserDAO {

    public UserAccount loginUser(String username, String password);

}
