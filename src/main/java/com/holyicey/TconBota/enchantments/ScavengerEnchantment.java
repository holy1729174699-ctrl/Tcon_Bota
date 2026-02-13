package com.holyicey.TconBota.enchantments;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * 拾荒者附魔 (Scavenger)
 * <p>
 * 效果：
 * 击杀实体时，有概率让实体额外掉落一次战利品表中的物品。
 * 概率 = 附魔等级 * 2.5%
 * <p>
 * 实现原理：
 * 使用反射调用 LivingEntity 的 dropFromLootTable 方法。
 */
public class ScavengerEnchantment extends Enchantment {

    private static final MethodHandle dropFromLootTable;

    // 静态代码块初始化反射方法句柄
    static {
        // 使用 SRG 名称 "m_7625_" 以确保在生产环境（混淆环境）中也能正常工作。
        // 对应 MCP 名称: dropFromLootTable
        Method m = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "m_7625_", DamageSource.class, boolean.class);
        try {
            m.setAccessible(true);
            dropFromLootTable = MethodHandles.lookup().unreflect(m);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("LivingEntity#dropFromLootTable (m_7625_) not located!", e);
        }
    }

    public ScavengerEnchantment() {
        super(
                Rarity.VERY_RARE,               // 稀有度：极稀有
                EnchantmentCategory.WEAPON,     // 适用类别：武器
                new EquipmentSlot[]{EquipmentSlot.MAINHAND} // 适用槽位：主手
        );
    }

    /**
     * 获取最小附魔消耗
     */
    @Override
    public int getMinCost(int level) {
        return 55 + level * level * 12; // 57 / 103 / 163
    }

    /**
     * 获取最大附魔消耗
     */
    @Override
    public int getMaxCost(int level) {
        return 200;
    }

    /**
     * 获取最大等级
     */
    @Override
    public int getMaxLevel() {
        return 5;
    }

    /**
     * 获取带颜色的全名
     */
    @Override
    public Component getFullname(int level) {
        return ((MutableComponent) super.getFullname(level)).withStyle(ChatFormatting.DARK_GREEN);
    }

    /**
     * 执行掉落逻辑
     * <p>
     * 此方法应由事件处理器调用。
     *
     * @param entity 被击杀的实体
     * @param source 伤害来源
     * @throws Throwable 如果反射调用失败
     */
    public void executeDrop(LivingEntity entity, DamageSource source) throws Throwable {
        // 调用 dropFromLootTable 方法
        // 第二个参数 boolean pAttackedRecently 通常为 true，因为只有被玩家攻击（近期被攻击）才会触发此逻辑
        dropFromLootTable.invoke(entity, source, true);
    }
}
