package com.holyicey.TconBota.modifiers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.CuriosApi;
import vazkii.botania.api.BotaniaForgeCapabilities;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mana Blast (魔力冲击) 修饰符
 * 效果：根据自身拥有的 mana 值造成 1% mana 值的魔法伤害。
 */
public class ManaBlastModifier extends BaseModifier {
    @Override
    protected float onModifyMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                        LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        if (attacker instanceof Player player) {
            // 尝试获取玩家物品栏和饰品栏中总魔力值
            try {
                AtomicInteger totalMana = new AtomicInteger(0);
                
                // 1. 遍历玩家主物品栏
                player.getInventory().items.forEach(stack -> {
                    addManaFromStack(stack, totalMana);
                });

                // 2. 遍历 Curios 饰品栏
                CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(handler -> {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        addManaFromStack(stack, totalMana);
                    }
                });
                
                // 计算额外伤害：1% 的魔力值
                float bonusDamage = totalMana.get() * 0.01f; 
                return actualDamage + bonusDamage;
            } catch (Throwable e) {
                return actualDamage;
            }
        }
        return actualDamage;
    }

    private void addManaFromStack(ItemStack stack, AtomicInteger totalMana) {
        if (!stack.isEmpty()) {
            stack.getCapability(BotaniaForgeCapabilities.MANA_ITEM).ifPresent(manaItem -> {
                totalMana.addAndGet(manaItem.getMana());
            });
        }
    }
}
