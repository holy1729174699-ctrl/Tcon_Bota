package com.holyicey.TconBota.modifiers;

import com.holyicey.TconBota.config.TconBotaConfig;


import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fml.ModList;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JMModifier extends BaseModifier {
    // ============================================
    // 构造函数
    // ============================================

    public JMModifier() {
        super();
    }

    // ============================================
    // 工具提示
    // ============================================

    /**
     * 添加工具提示
     */
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player,
                           List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int level = modifier.getLevel();
        // 使用配置中的比率
        double ratio = TconBotaConfig.getJmManaRatio();
        float manaPerDamage = (float) (level * ratio);

        // 显示魔力转换率
        tooltip.add(Component.translatable("modifier.tconbota.jm.mana_desc")
                .append(Component.literal(String.format(": 获得 %.1f 魔力/伤害", manaPerDamage)))
                .withStyle(net.minecraft.ChatFormatting.AQUA));

        // 添加说明
        tooltip.add(Component.literal("  基于实际造成伤害获得魔力 (支持饰品栏生效)")
                .withStyle(net.minecraft.ChatFormatting.GRAY));
    }

    // ============================================
    // 优先级
    // ============================================
    
    // TConstruct 3 通过 hook 注册顺序或 hook 参数控制优先级，
    // 单纯的 getPriority() 方法可能已被移除或不再起作用。
    // 由于主要逻辑在 Event Handler 中处理，此处移除该方法。
}