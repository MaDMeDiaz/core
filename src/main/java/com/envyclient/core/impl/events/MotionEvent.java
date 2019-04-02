package com.envyclient.core.impl.events;

import me.ihaq.eventmanager.Event;
import me.ihaq.eventmanager.data.EventType;
import me.ihaq.eventmanager.type.Type;
import net.minecraft.util.MathHelper;

import java.util.Random;

public class MotionEvent extends Event implements Type {

    private static final Random random = new Random();

    private EventType type;
    private float yaw, pitch;
    private boolean ground;
    private double y;

    public MotionEvent(EventType type, float yaw, float pitch, boolean ground, double y) {
        this.type = type;
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.y = y;
    }

    @Override
    public EventType getType() {
        return type;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isGround() {
        return ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setRotations(float[] rotations, boolean random) {
        if (random) {
            yaw = rotations[0] + (float) (MotionEvent.random.nextBoolean() ? Math.random() : -Math.random());
            pitch = rotations[1] + (float) (MotionEvent.random.nextBoolean() ? Math.random() : -Math.random());
        } else {
            yaw = rotations[0];
            pitch = rotations[1];
        }
    }

    public void setRotations(float[] rotations, boolean random, float minPitch, float maxPitch) {
        float yaw = rotations[0];
        float pitch = rotations[1];

        if (random) {
            yaw = yaw + (float) (MotionEvent.random.nextBoolean() ? Math.random() : -Math.random());
            pitch = pitch + (float) (MotionEvent.random.nextBoolean() ? Math.random() * 2 : -Math.random() * 2);
        }

        this.yaw = MathHelper.wrapAngleTo180_float(yaw);
        this.pitch = pitch;

        if (pitch > maxPitch) {
            this.pitch = maxPitch;
        } else if (pitch < minPitch) {
            this.pitch = minPitch;
        }
    }

}
