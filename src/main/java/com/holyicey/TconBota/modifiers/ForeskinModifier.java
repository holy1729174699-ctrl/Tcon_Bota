package com.holyicey.TconBota.modifiers;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

/**
 * Foreskin (包皮) 修饰符
 * 效果：增加 20% 耐久
 */
public class ForeskinModifier extends BaseModifier {
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        // 每一级增加 20% 耐久
        ToolStats.DURABILITY.multiplyAll(builder, 1 + (0.2f * modifier.getLevel()));
    }
}
