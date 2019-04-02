package com.envyclient.core.api.module;

import com.envyclient.core.api.module.data.Data;
import com.envyclient.core.api.module.type.Category;
import com.envyclient.core.api.setting.data.Name;
import com.envyclient.core.util.Timer;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.ihaq.eventmanager.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

import java.util.ArrayList;
import java.util.List;

import static com.envyclient.core.Envy.Managers.EVENT;

public abstract class Module implements EventListener {

    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected static final net.minecraft.util.Timer mcTimer = Minecraft.getMinecraft().getTimer();

    protected final Timer timer = new Timer();

    private final Category category = getClass().getAnnotation(Data.class).category();
    private final String name = getClass().getAnnotation(Data.class).name();
    private int keyCode = getClass().getAnnotation(Data.class).keyCode();
    private final String description = getClass().getAnnotation(Data.class).description();
    private boolean toggled = getClass().getAnnotation(Data.class).toggled();
    private final String[] alias = getClass().getAnnotation(Data.class).alias();

    @Name("Mode")
    private String currentMode;
    private List<ModuleMode> modes = new ArrayList<>();

    private String displayText;

    protected void onEnable() {
        timer.reset();
        EVENT.register(this);
        mcTimer.timerSpeed = 1;

        // disabling all modes
        modes.forEach(ModuleMode::onDisable);

        // getting the current mode and enabling it
        ModuleMode mode = getCurrentMode();
        if (mode != null) {
            mode.onEnable();
        }
    }

    protected void onDisable() {
        timer.reset();
        EVENT.unregister(this);
        mcTimer.timerSpeed = 1;

        // disabling all modes
        modes.forEach(ModuleMode::onDisable);
    }

    protected void addMode(ModuleMode mode) {
        modes.add(mode);
    }

    protected void sendPacket(Packet packet) {
        if (mc.thePlayer != null) {
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
    }

    protected void sendMessage(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.sendMessage(message);
        }
    }

    public void toggle() {
        if (toggled) {
            onDisable();
        } else {
            onEnable();
        }
        toggled = !toggled;
    }

    public ModuleMode getCurrentMode() {
        return modes.stream()
                .filter(mode -> mode.getName().equalsIgnoreCase(currentMode))
                .findFirst().orElse(null);
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        if (currentMode == null && displayText == null) {
            return name;
        } else if (currentMode == null) {
            return name + ChatFormatting.WHITE + " : " + displayText;
        } else {
            return name + ChatFormatting.WHITE + " : " + currentMode;
        }
    }

    protected void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keycode) {
        this.keyCode = keycode;
    }

    public boolean isToggled() {
        return toggled;
    }

    public String[] getAlias() {
        return alias;
    }

    public List<ModuleMode> getModes() {
        return modes;
    }
}