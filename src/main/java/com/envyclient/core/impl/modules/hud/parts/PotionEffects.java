package com.envyclient.core.impl.modules.hud.parts;

import com.envyclient.core.impl.events.Render2DEvent;
import com.envyclient.core.impl.modules.hud.Hud;
import com.envyclient.core.util.render.FontUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.ihaq.eventmanager.listener.EventListener;
import me.ihaq.eventmanager.listener.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public class PotionEffects implements EventListener {

    private Hud hud;

    public PotionEffects(Hud hud) {
        this.hud = hud;
    }

    @EventTarget
    public void onRender(Render2DEvent e) {

        if (!hud.potionEffects) {
            return;
        }

        GlStateManager.pushMatrix();

        int size = 16;

        int yOffset = 10;

        if (Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen())
            yOffset = -3;

        float x = (e.getWidth() - size * 2) + 8;
        float y = (e.getHeight() - size * 2) + yOffset;

        Collection<PotionEffect> effects = Minecraft.getMinecraft().thePlayer.getActivePotionEffects();

        int i = 0;
        if (!effects.isEmpty()) {

            for (PotionEffect potionEffect : effects) {

                Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                int potionDuration = potionEffect.getDuration();
                String potionDurationFormatted = Potion.getDurationString(potionEffect);

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));

                if (potion.hasStatusIcon()) {
                    int var9 = potion.getStatusIconIndex();
                    Gui.INSTANCE.drawTexturedModalRect((int) x, (int) y - (18 * i), var9 % 8 * 18, 198 + var9 / 8 * 18, 18, 18);
                    FontUtils.drawStringWithShadow("" + (potionDuration <= 300 ? ChatFormatting.RED : ChatFormatting.WHITE) + Potion.getDurationString(potionEffect), (double) x - FontUtils.getStringWidth(potionDurationFormatted) - 5, (double) y - (18 * i) + 6, -1);
                    i++;
                }

            }
        }

        GlStateManager.popMatrix();

    }
}
