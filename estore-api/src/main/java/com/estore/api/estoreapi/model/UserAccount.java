package com.estore.api.estoreapi.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccount {
    public final static String AUTH_SEPARATOR = "\\*";
    
    @NotNull
    @JsonProperty
    private Integer id;

    @NotNull
    @JsonProperty
    private String username;

    @JsonProperty
    private Cart cart;

    @JsonProperty
    private boolean isAdmin;

    public String getUsername() {
        return this.username;
    }

    public int getId() {
        return this.id.intValue();
    }

    public String toString() {
        return String.format("UserAccount [username='%s', id='%d', isAdmin='%b']", username, id, isAdmin);
    }
}
