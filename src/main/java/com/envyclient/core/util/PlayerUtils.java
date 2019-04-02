package com.envyclient.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

import static com.envyclient.core.Envy.Managers.CLICK_GUI;

public class PlayerUtils {

    private static Minecraft mc = Minecraft.getMinecraft();

    private PlayerUtils() {
    }

    public static int getPing(EntityPlayer player) {
        try {
            return mc.thePlayer.sendQueue.getPlayerInfo(player.getUniqueID()).getResponseTime();
        } catch (Exception ignored) {
            return 0;
        }
    }

    private static String getTabName(EntityPlayer player) {
        String realName = "";
        for (NetworkPlayerInfo playerInfo : GuiPlayerTabOverlay.getPlayerListTeams()) {
            String mcName = mc.ingameGUI.getTabList().getPlayerName(playerInfo);
            if (mcName.contains(player.getName()) && !Objects.equals(player.getName(), mcName)) {
                realName = mcName;
            }
        }
        return realName;
    }

    public static boolean onSameTeam(Entity entity) {
        if (entity instanceof EntityPlayer
                && PlayerUtils.getTabName((EntityPlayer) entity).length() > 2
                && PlayerUtils.getTabName(mc.thePlayer).startsWith(PlayerUtils.getTabName((EntityPlayer) entity).substring(0, 2))) {
            return true;
        }
        return false;
    }

    public static void sendConsoleReply(String message) {
        CLICK_GUI.getConsole().addReply(message, true);
    }

    public static void dropItemInInventory(int slot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, mc.thePlayer);
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
    }

    public static void playButtonSound() {
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1));
    }

}