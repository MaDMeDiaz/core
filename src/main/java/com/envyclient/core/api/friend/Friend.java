package com.envyclient.core.api.friend;

import com.google.gson.annotations.SerializedName;

public class Friend {

    @SerializedName("name")
    private String name;

    @SerializedName("alias")
    private String alias;

    public Friend(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}