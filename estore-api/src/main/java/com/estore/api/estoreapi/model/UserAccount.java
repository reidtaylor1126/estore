package com.estore.api.estoreapi.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccount {
    @NotNull
    @JsonProperty
    private int id;

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
}
