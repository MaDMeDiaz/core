package com.envyclient.core.impl.events;

import me.ihaq.eventmanager.Event;

public class Render2DEvent extends Event {

    private int width, height;

    public Render2DEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
