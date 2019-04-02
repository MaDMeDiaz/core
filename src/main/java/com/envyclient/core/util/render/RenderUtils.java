package com.envyclient.core.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

    private static final Frustum frustrum = new Frustum();

    private RenderUtils() {
    }

    public static void prepareScissorBox(double x, double y, double x2, double y2) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int factor = sr.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((sr.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public static Vec3 getRenderPosition(Entity entity) {
        float x = (float) (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().getTimer().renderPartialTicks - Minecraft.getMinecraft().getRenderManager().renderPosX);
        float y = (float) (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Minecraft.getMinecraft().getTimer().renderPartialTicks - Minecraft.getMinecraft().getRenderManager().renderPosY);
        float z = (float) (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().getTimer().renderPartialTicks - Minecraft.getMinecraft().getRenderManager().renderPosZ);
        y += (float) (0.4 + entity.getEyeHeight() - (entity.isSneaking() ? 0.2 : 0.0));
        return new Vec3(x, y, z);
    }

    public static boolean isInFrustumView(Entity ent) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        double x = interpolate(current.posX, current.lastTickPosX),
                y = interpolate(current.posY, current.lastTickPosY),
                z = interpolate(current.posZ, current.lastTickPosZ);
        frustrum.setPosition(x, y, z);
        return frustrum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) || ent.ignoreFrustumCheck;
    }

    private static double interpolate(double newPos, double oldPos) {
        return oldPos + (newPos - oldPos) * Minecraft.getMinecraft().getTimer().renderPartialTicks;
    }

}