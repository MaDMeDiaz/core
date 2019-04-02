package com.envyclient.core.impl.events;

import me.ihaq.eventmanager.Event;
import me.ihaq.eventmanager.type.Cancellable;
import net.minecraft.network.Packet;

public class PacketReceiveEvent extends Event implements Cancellable {

    private boolean cancelled;
    private Packet packet;

    public PacketReceiveEvent(Packet packet) {
        this.packet = packet;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Packet getPacket() {
        return packet;
    }
}
