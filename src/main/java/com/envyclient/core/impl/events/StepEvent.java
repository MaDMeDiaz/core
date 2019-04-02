package com.envyclient.core.impl.events;

import me.ihaq.eventmanager.Event;
import net.minecraft.entity.Entity;

public class StepEvent extends Event {
    private float stepHeight;
    private Entity entity;

    public StepEvent(Entity entity, float stepHeight) {
        this.entity = entity;
        this.stepHeight = stepHeight;
    }

    public float getStepHeight() {
        return this.stepHeight;
    }

    public void setStepHeight(float stepHeight) {
        this.stepHeight = stepHeight;
    }

    public Entity getEntity() {
        return this.entity;
    }

}
