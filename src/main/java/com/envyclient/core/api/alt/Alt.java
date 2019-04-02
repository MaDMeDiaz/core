package com.envyclient.core.api.alt;

import com.google.gson.annotations.SerializedName;

public class Alt {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public Alt(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}