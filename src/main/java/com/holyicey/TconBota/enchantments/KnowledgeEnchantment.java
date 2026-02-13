package com.holyicey.TconBota.enchantments;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * 阅历附魔 (Knowledge)
 * <p>
 * 效果：
 * 击杀非玩家实体时，清空掉落物并将物品数量转换为经验值。
 * 经验值 = 物品总数 * 附魔等级 * 25
 */
public class KnowledgeEnchantment extends Enchantment {

    // ============================================
    // 常量定义
    // ============================================

    /** 基础附魔消耗 */
    private static final int MIN_COST_BASE = 15;

    /** 每级额外消耗 */
    private static final int COST_PER_LEVEL = 9;

    /** 消耗区间 */
    private static final int COST_RANGE = 50;

    /** 最大等级 */
    private static final int MAX_LEVEL = 5;

    /** 适用槽位 */
    private static final EquipmentSlot[] APPLICABLE_SLOTS = new EquipmentSlot[]{EquipmentSlot.MAINHAND};

    // ============================================
    // 构造函数
    // ============================================

    public KnowledgeEnchantment() {
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
     * 获取最小附魔消耗
     */
    @Override
    public int getMinCost(int level) {
        return MIN_COST_BASE + (level - 1) * COST_PER_LEVEL;
    }

    /**
     * 获取最大附魔消耗
     */
    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + COST_RANGE;
    }

    /**
     * 获取最大等级
     */
    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    /**
     * 检查是否可以附魔
     */
    @Override
    public boolean canEnchant(ItemStack stack) {
        return !stack.isEmpty() && super.canEnchant(stack);
    }

    @Override
    public Component getFullname(int level) {
        return ((MutableComponent) super.getFullname(level)).withStyle(ChatFormatting.DARK_GREEN);
    }
    /**
     * 检查是否与其他附魔兼容
     * 通常不与抢夺 (Looting) 兼容，因为机制冲突（抢夺增加掉落，阅历消除掉落）
     * 但用户未明确说明，这里暂时保持默认兼容性，或者显式互斥。
     * 考虑到逻辑（清空掉落），抢夺增加的掉落物也会被转化为经验，所以逻辑上是兼容的（抢夺能增加经验收益）。
     * 所以这里不做互斥处理。
     */
    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other);
    }
}
