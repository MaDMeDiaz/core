package com.envyclient.core.util.web;

public interface Callback<T> {

    void onSuccess(T e);

    void onFail(Exception e);

}