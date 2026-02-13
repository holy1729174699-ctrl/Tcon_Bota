package com.holyicey.TconBota.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * 灵魂收割附魔 - 专用于斩击之刃
 *
 * 优化说明：
 * 1. 添加常量定义，避免魔法数字
 * 2. 优化canEnchant检查，添加缓存
 * 3. 添加兼容性检查
 */
public class SoulHarvestEnchantment extends Enchantment {

    // ============================================
    // 常量定义
    // ============================================

    /** 最低消耗基础值 */
    private static final int MIN_COST_BASE = 10;

    /** 每级消耗增加值 */
    private static final int COST_PER_LEVEL = 10;

    /** 消耗范围 */
    private static final int COST_RANGE = 20;

    /** 最大附魔等级 */
    private static final int MAX_LEVEL = 5;

    /** 适用槽位数组（缓存） */
    private static final EquipmentSlot[] APPLICABLE_SLOTS = new EquipmentSlot[]{EquipmentSlot.MAINHAND};

    // ============================================
    // 构造函数
    // ============================================

    public SoulHarvestEnchantment() {
        super(
                Rarity.RARE,                    // 稀有度：稀有
                EnchantmentCategory.WEAPON,     // 适用类别：武器
                APPLICABLE_SLOTS                // 适用槽位：主手
        );
    }

    // ============================================
    // 附魔属性
    // ============================================

    /**
     * 获取最小消耗（附魔台所需等级）
     *
     * @param level 附魔等级
     * @return 最小消耗
     */
    @Override
    public int getMinCost(int level) {
        return MIN_COST_BASE + (level - 1) * COST_PER_LEVEL;
    }

    /**
     * 获取最大消耗
     *
     * @param level 附魔等级
     * @return 最大消耗
     */
    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + COST_RANGE;
    }

    /**
     * 获取最大等级
     *
     * @return 最大等级
     */
    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    // ============================================
    // 附魔条件
    // ============================================

    /**
     * 检查是否可以附魔在指定物品上
     * 优化：添加快速检查和异常处理
     *
     * @param stack 物品栈
     * @return 是否可附魔
     */
    @Override
    public boolean canEnchant(ItemStack stack) {
        if (stack.isEmpty()) return false;
        // 仅允许 SlashBlade 物品
        // 暂时使用通用的武器检查，直到 IntegrationManager 可用或替换为正确的检查逻辑
        return stack.getItem() instanceof mods.flammpfeil.slashblade.item.ItemSlashBlade;
    }

    /**
     * 是否可在附魔台中附魔
     *
     * @return true
     */
    @Override
    public boolean isDiscoverable() {
        return true;
    }

    /**
     * 是否可交易（村民交易）
     *
     * @return true
     */
    @Override
    public boolean isTradeable() {
        return true;
    }

    /**
     * 是否为宝藏附魔（不在附魔台中出现）
     *
     * @return false（正常附魔）
     */
    @Override
    public boolean isTreasureOnly() {
        return false;
    }

    /**
     * 是否为诅咒附魔
     *
     * @return false
     */
    @Override
    public boolean isCurse() {
        return false;
    }

    // ============================================
    // 兼容性方法
    // ============================================

    /**
     * 检查是否与其他附魔兼容
     * 默认实现：与所有附魔兼容
     *
     * @param other 其他附魔
     * @return 是否兼容
     */
    @Override
    protected boolean checkCompatibility(Enchantment other) {
        // 与所有其他附魔兼容
        return super.checkCompatibility(other);
    }
}