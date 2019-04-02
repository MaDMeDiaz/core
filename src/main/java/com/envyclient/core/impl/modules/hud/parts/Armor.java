package com.envyclient.core.impl.modules.hud.parts;

import com.envyclient.core.impl.events.Render2DEvent;
import com.envyclient.core.impl.modules.hud.Hud;
import com.envyclient.core.util.render.FontUtils;
import me.ihaq.eventmanager.listener.EventListener;
import me.ihaq.eventmanager.listener.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.*;

public class Armor implements EventListener {

    private Hud hud;

    public Armor(Hud hud) {
        this.hud = hud;
    }

    @EventTarget
    public void onRender(Render2DEvent e) {

        if (!hud.armor) {
            return;
        }

        int itemX = e.getWidth() / 2 + 9;

        for (int i = 0; i < 5; i++) {

            ItemStack ia = Minecraft.getMinecraft().thePlayer.getEquipmentInSlot(i);

            if (ia == null)
                continue;

            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            float oldZ = Minecraft.getMinecraft().getRenderItem().zLevel;
            Minecraft.getMinecraft().getRenderItem().zLevel = -100;
            Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(ia, itemX, e.getHeight() - 55);
            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, ia, itemX, e.getHeight() - 55);
            Minecraft.getMinecraft().getRenderItem().zLevel = oldZ;

            if (ia.getItem() instanceof ItemSword || ia.getItem() instanceof ItemTool || ia.getItem() instanceof ItemArmor || ia.getItem() instanceof ItemBow) {
                int durability = ia.getMaxDamage() - ia.getItemDamage();
                int y = e.getHeight() - 60;
                GlStateManager.scale(0.5, 0.5, 0.5);
                FontUtils.drawStringWithShadow(durability + "", (itemX + 4) / (float) 0.5, ((float) y) / (float) 0.5, -1);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();

            itemX += 16;
        }
    }
}
