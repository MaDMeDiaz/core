package com.envyclient.core.api.module;

import com.envyclient.core.util.Timer;
import me.ihaq.eventmanager.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

import static com.envyclient.core.Envy.Managers.EVENT;

public abstract class ModuleMode<T extends Module> implements EventListener {

    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected static final net.minecraft.util.Timer mcTimer = Minecraft.getMinecraft().getTimer();

    private String name;
    private T module;

    public ModuleMode(String name, T module) {
        this.name = name;
        this.module = module;
    }

    protected void onEnable() {
        EVENT.register(this);
    }

    protected void onDisable() {
        EVENT.unregister(this);
    }

    protected void sendPacket(Packet packet) {
        module.sendPacket(packet);
    }

    protected void sendMessage(String message) {
        module.sendMessage(message);
    }

    protected Timer getTimer() {
        return module.timer;
    }

    public String getName() {
        return name;
    }

    public T getModule() {
        return module;
    }

}