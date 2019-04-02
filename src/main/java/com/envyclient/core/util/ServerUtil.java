package com.envyclient.core.util;

import net.minecraft.client.Minecraft;

public class ServerUtil {

    private ServerUtil() {
    }

    public static boolean isHypixel() {
        return (Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP.toLowerCase().contains("hypixel"));
    }

    public static boolean isCubeCraft() {
        return (Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP.toLowerCase().contains("cubecraft"));
    }

    public static boolean isMineplex() {
        return (Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP.toLowerCase().contains("mineplex"));
    }

}
