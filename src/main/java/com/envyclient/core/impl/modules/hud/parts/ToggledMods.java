package com.envyclient.core.impl.modules.hud.parts;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.impl.events.Render2DEvent;
import com.envyclient.core.impl.modules.hud.Hud;
import com.envyclient.core.util.render.FontUtils;
import me.ihaq.eventmanager.listener.EventListener;
import me.ihaq.eventmanager.listener.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.util.List;

import static com.envyclient.core.Envy.Colors.MAIN;
import static com.envyclient.core.Envy.Colors.SECONDARY;
import static com.envyclient.core.Envy.Managers.MODULE;

public class ToggledMods implements EventListener {

    private Hud hud;

    public ToggledMods(Hud hud) {
        this.hud = hud;
    }

    @EventTarget
    public void onRender(Render2DEvent e) {
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            return;
        }

        if (!hud.toggledMods) {
            return;
        }

        GlStateManager.pushMatrix();

        int yStart = 1;

        List<Module> mods = MODULE.getToggledModules();
        mods.sort((o1, o2) -> FontUtils.getStringWidth(o2.getDisplayName()) - FontUtils.getStringWidth(o1.getDisplayName()));

        for (Module module : mods) {

            int startX = e.getWidth() - FontUtils.getStringWidth(module.getDisplayName()) - 6;

            Gui.drawRect(startX, yStart - 1, e.getWidth(), yStart + FontUtils.getFontHeight(), SECONDARY);
            Gui.drawRect(e.getWidth() - 2, yStart - 1, e.getWidth(), yStart + FontUtils.getFontHeight(), MAIN);

            Gui.drawVerticalLine(startX - 1, yStart - 2, yStart + FontUtils.getFontHeight(), MAIN);
            Gui.drawHorizontalLine(startX - 1, e.getWidth(), yStart + FontUtils.getFontHeight(), MAIN);

            FontUtils.drawStringWithShadow(module.getDisplayName(), startX + 3, yStart, MAIN);

            yStart += FontUtils.getFontHeight() + 1;
        }

        GlStateManager.popMatrix();
    }
}
