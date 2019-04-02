package com.envyclient.core.impl.events;

import me.ihaq.eventmanager.Event;

public class Render3DEvent extends Event {

    private float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
