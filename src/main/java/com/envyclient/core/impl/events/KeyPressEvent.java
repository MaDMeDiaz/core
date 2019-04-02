package com.envyclient.core.impl.events;

import me.ihaq.eventmanager.Event;

public class KeyPressEvent extends Event {

    private int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
