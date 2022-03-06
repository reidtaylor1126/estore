package com.estore.api.estoreapi.persistence;

public interface UserDAO {

    public UserAccount loginUser(string username, string password) throws IOException;

}
