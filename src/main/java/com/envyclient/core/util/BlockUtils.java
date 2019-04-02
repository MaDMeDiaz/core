package com.envyclient.core.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class BlockUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private BlockUtils() {
    }

    public static Block getBlock(double x, double y, double z) {
        return getBlockState(x, y, z).getBlock();
    }

    public static IBlockState getBlockState(double x, double y, double z) {
        return mc.theWorld.getBlockState(new BlockPos(x, y, z));
    }

    public static Block getBlock(BlockPos pos) {
        return getBlockState(pos.getX(), pos.getY(), pos.getZ()).getBlock();
    }
}
