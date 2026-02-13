package com.holyicey.TconBota.integration;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Curios兼容层 - 隔离直接的类依赖
 * 仅在Curios加载时调用此类方法
 */
public class CuriosCompat {

    public static int getCuriosModifierLevel(Player player, String modifierIdStr) {
        AtomicInteger level = new AtomicInteger(0);
        ModifierId modifierId = new ModifierId(modifierIdStr);
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.getCurios().forEach((id, stackHandler) -> {
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(i);
                    int itemLevel = ModifierUtil.getModifierLevel(stack, modifierId);
                    if (itemLevel > 0) {
                        level.addAndGet(itemLevel);
                    }
                }
            });
        });
        return level.get();
    }
}
