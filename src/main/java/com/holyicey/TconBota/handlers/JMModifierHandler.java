package com.holyicey.TconBota.handlers;

import com.holyicey.TconBota.TconBota;
import com.holyicey.TconBota.config.TconBotaConfig;
import com.holyicey.TconBota.init.TconBotaModifiers;
import com.holyicey.TconBota.integration.CuriosCompat;
import com.holyicey.TconBota.integration.IntegrationManager;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import vazkii.botania.api.mana.ManaItemHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * JM词条功能处理器
 * 负责处理伤害转魔力的逻辑
 */
public class JMModifierHandler {

    /**
     * 处理伤害事件
     * 使用LivingDamageEvent以获取实际伤害值（在护甲和抗性计算之后）
     * 触发条件：
     * 1. 伤害来源是玩家
     * 2. 对任意实体造成伤害（无需区分类型）
     * 3. Botania已加载
     */
    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        // 仅处理玩家造成的伤害，且Botania必须加载
        if (!(event.getSource().getEntity() instanceof Player player) || !IntegrationManager.isBotaniaLoaded()) {
            return;
        }

        // 获取实际造成的伤害（经过护甲、抗性等减免后的最终数值）
        float actualDamage = event.getAmount();
        if (actualDamage <= 0) return;

        // 计算总词条等级
        int totalLevel = calculateTotalModifierLevel(player);

        if (totalLevel > 0) {
            dispatchMana(player, actualDamage, totalLevel);
        }
    }

    /**
     * 计算玩家身上所有装备（手持、护甲、饰品）的JM词条总等级
     */
    private int calculateTotalModifierLevel(Player player) {
        int level = 0;

        // 1. 检查主手和副手
        level += getModifierLevel(player.getMainHandItem());
        level += getModifierLevel(player.getOffhandItem());
        
        for (ItemStack armorStack : player.getArmorSlots()) {
        level += getModifierLevel(armorStack);
    }

        // 2. 检查饰品栏 (如果Curios已加载)
        if (IntegrationManager.isCuriosLoaded()) {
            level += CuriosCompat.getCuriosModifierLevel(player, TconBotaModifiers.jm.getId().toString());
        }

        return level;
    }

    /**
     * 获取单个物品的JM词条等级
     */
    private int getModifierLevel(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        
        // 尝试获取匠魂词条等级
        return ModifierUtil.getModifierLevel(stack, TconBotaModifiers.jm.getId());
    }

    /**
     * 分发魔力
     */
    private void dispatchMana(Player player, float damage, int level) {
        try {
            double ratio = TconBotaConfig.getJmManaRatio();
            int totalMana = Math.max(1, (int) (damage * level * ratio));
            
            ItemStack sourceStack = player.getMainHandItem();
            if (sourceStack.isEmpty()) {
                sourceStack = player.getOffhandItem();
            }
            
            // Botania 的 dispatchMana 已经处理了分配逻辑，不需要手动循环
            ManaItemHandler.instance().dispatchMana(
                sourceStack,
                player,
                totalMana,
                true // true 表示添加魔力
            );
            
        } catch (Exception e) {
            TconBota.LOGGER.error("Error dispatching mana in JMModifierHandler", e);
        }
    }
}
