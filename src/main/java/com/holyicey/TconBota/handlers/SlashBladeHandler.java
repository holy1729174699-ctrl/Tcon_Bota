package com.holyicey.TconBota.handlers;

import com.holyicey.TconBota.TconBota;
import com.holyicey.TconBota.init.EnchantmentInit;


import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * SlashBlade集成处理器
 * 负责处理与拔刀剑相关的事件和逻辑
 */
public class SlashBladeHandler {

    /** 经验值到骄魂转换系数（每级附魔） */
    private static final float PROUD_SOUL_MULTIPLIER = 1.0f;

    // ============================================
    // 事件处理器
    // ============================================

    /**
     * 监听实体死亡事件 - 增加杀敌数
     * 优先级设置为 LOWEST，确保在 SlashBlade 处理之后执行
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDeath(LivingDeathEvent event) {
        // 获取击杀者
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        // 获取主手物品
        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.isEmpty()) {
            return;
        }

        // 获取附魔等级
        int enchantLevel = getEnchantmentLevel(weapon);
        if (enchantLevel <= 0) {
            return;
        }

        // 修改杀敌数
        modifyBladeState(weapon, state -> {
            int currentKillCount = state.getKillCount();
            // 每级附魔额外增加 200% 的杀敌数（基数为 1）
            int bonus = enchantLevel * 2;
            state.setKillCount(currentKillCount + bonus);

            // 调试日志
            if (TconBota.LOGGER.isDebugEnabled()) {
                TconBota.LOGGER.debug("Soul Harvest Level {}: Added {} bonus kill count (total: {})",
                        enchantLevel, bonus, state.getKillCount());
            }
        });
    }

    /**
     * 监听经验掉落事件 - 增加骄傲之魂
     * 优先级：LOWEST，确保在SlashBlade处理之后执行
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onExperienceDrop(LivingExperienceDropEvent event) {
        // 获取攻击玩家
        Player player = event.getAttackingPlayer();
        if (player == null) {
            return;
        }

        // 获取主手物品
        ItemStack weapon = player.getMainHandItem();
        if (weapon.isEmpty()) {
            return;
        }

        // 获取附魔等级
        int enchantLevel = getEnchantmentLevel(weapon);
        if (enchantLevel <= 0) {
            return;
        }

        // 计算骄魂增量
        int baseIncrease = event.getDroppedExperience();
        if (baseIncrease <= 0) {
            return;
        }

        // 每级附魔增加100%的骄傲之魂
        int bonus = (int) Math.floor(baseIncrease * enchantLevel * PROUD_SOUL_MULTIPLIER);

        // 修改骄傲之魂数量
        modifyBladeState(weapon, state -> {
            int currentProudSoul = state.getProudSoulCount();
            state.setProudSoulCount(currentProudSoul + bonus);

            // 调试日志
            if (TconBota.LOGGER.isDebugEnabled()) {
                TconBota.LOGGER.debug("Soul Harvest Level {}: Added {} bonus proud soul (total: {})",
                        enchantLevel, bonus, state.getProudSoulCount());
            }
        });
    }

    // ============================================
    // 工具方法
    // ============================================

    /**
     * 获取物品栈的灵魂收割附魔等级
     */
    private int getEnchantmentLevel(ItemStack stack) {
        if (stack.isEmpty()) return 0;

        // 检查是否为斩击之刃 (无需再次检查Mod加载，因为此类只在SlashBlade加载时注册)
        if (!stack.getCapability(ItemSlashBlade.BLADESTATE).isPresent()) {
            return 0;
        }

        try {
            return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_HARVEST.get(), stack);
        } catch (Exception e) {
            TconBota.LOGGER.error("Failed to get enchantment level", e);
            return 0;
        }
    }

    /**
     * 安全地修改刀状态
     */
    private boolean modifyBladeState(ItemStack stack, NonNullConsumer<? super ISlashBladeState> modifier) {
        try {
            stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(modifier);
            return true;
        } catch (Exception e) {
            TconBota.LOGGER.error("Failed to modify blade state", e);
            return false;
        }
    }
}
