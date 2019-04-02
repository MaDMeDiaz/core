package com.envyclient.core.impl.events;

import me.ihaq.eventmanager.Event;
import me.ihaq.eventmanager.type.Cancellable;
import net.minecraft.network.Packet;

public class PacketSendEvent extends Event implements Cancellable {

    private boolean cancelled;
    private Packet packet;

    public PacketSendEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
