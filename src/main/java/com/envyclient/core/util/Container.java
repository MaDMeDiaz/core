package com.envyclient.core.util;

import java.util.ArrayList;
import java.util.List;

public abstract class Container<T> {

    private List<T> contents = new ArrayList<>();

    public void add(T e) {
        contents.add(e);
    }

    public void remove(T e) {
        contents.remove(e);
    }

    public void clear() {
        contents.clear();
    }

    public List<T> getContents() {
        return contents;
    }

    public void setContents(List<T> contents) {
        this.contents = contents;
    }
}