package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.UserAccount;
import java.io.IOException;
import com.estore.api.estoreapi.model.UserAccount;

public interface UserDAO {

    public UserAccount loginUser(String username);

    public UserAccount createUser(UserAccount user) throws IOException;
}
