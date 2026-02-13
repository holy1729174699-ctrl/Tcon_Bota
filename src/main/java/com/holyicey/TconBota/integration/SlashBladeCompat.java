package com.holyicey.TconBota.integration;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.item.ItemStack;

/**
 * SlashBlade兼容层 - 隔离直接的类依赖
 * 仅在SlashBlade加载时调用此类方法
 */
public class SlashBladeCompat {

    public static boolean isSlashBlade(ItemStack stack) {
        if (stack.isEmpty()) return false;
        try {
            return stack.getCapability(ItemSlashBlade.BLADESTATE).isPresent();
        } catch (Exception e) {
            return false;
        }
    }
}
