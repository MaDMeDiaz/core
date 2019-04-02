package com.envyclient.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;

public class RotationUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private RotationUtils() {
    }

    private static float[] getRotations(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        double distance = difference.flat().lengthVector();
        float yaw = (float) Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(Vec3 position) {
        return getRotations(mc.thePlayer.getPositionVector().addVector(0.0D, (double) mc.thePlayer.getEyeHeight(), 0.0D), position);
    }

    public static float[] getRotations(Entity entity) {
        return getRotations(mc.thePlayer.getPositionVector().addVector(0.0D, (double) mc.thePlayer.getEyeHeight(), 0.0D), entity.getPositionVector().addVector(0.0D, (double) (entity.getEyeHeight()), 0.0D));
    }

    public static float[] getRotationsBlock(BlockPos pos) {
        return getRotations(mc.thePlayer.getPositionVector().addVector(0.0D, (double) mc.thePlayer.getEyeHeight(), 0.0D), new Vec3((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D));
    }

    public static EnumFacing getFacingDirection(BlockPos pos) {
        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
        EnumFacing direction = null;
        if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isNormalCube()) {
            direction = EnumFacing.UP;
        } else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isNormalCube()) {
            direction = EnumFacing.DOWN;
        } else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isNormalCube()) {
            direction = EnumFacing.EAST;
        } else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isNormalCube()) {
            direction = EnumFacing.WEST;
        } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isNormalCube()) {
            direction = EnumFacing.SOUTH;
        } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isNormalCube()) {
            direction = EnumFacing.NORTH;
        }
        MovingObjectPosition rayResult = mc.theWorld.rayTraceBlocks(new Vec3(p.posX, p.posY + p.getEyeHeight(), p.posZ), new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
        if (rayResult != null && rayResult.getBlockPos() == pos) {
            return rayResult.sideHit;
        }
        return direction;
    }
}
