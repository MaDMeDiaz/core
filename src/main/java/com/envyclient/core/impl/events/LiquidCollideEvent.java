package com.envyclient.core.impl.events;

import me.ihaq.eventmanager.Event;
import net.minecraft.util.AxisAlignedBB;

public class LiquidCollideEvent extends Event {

    private AxisAlignedBB boundingBox;
    private int x, y, z;

    public LiquidCollideEvent(AxisAlignedBB boundingBox, int x, int y, int z) {
        this.boundingBox = boundingBox;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

}
