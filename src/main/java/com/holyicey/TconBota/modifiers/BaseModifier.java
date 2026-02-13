package com.holyicey.TconBota.modifiers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.DamageDealtModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.RequirementsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.EntityInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.KeybindInteractModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * 基础修饰符抽象类 - 提供所有常用钩子的默认实现
 * 优化说明：
 * 1. 移除不必要的事件总线监听器注册（改为按需在子类中注册）
 * 2. 添加服务器端检查，避免客户端不必要的计算
 * 3. 简化方法链，减少方法调用开销
 */
public abstract class BaseModifier extends Modifier implements
        AttributesModifierHook,
        ProcessLootModifierHook,
        MeleeDamageModifierHook,
        MeleeHitModifierHook,
        DamageDealtModifierHook,
        BowAmmoModifierHook,
        ProjectileHitModifierHook,
        ProjectileLaunchModifierHook,
        EquipmentChangeModifierHook,
        InventoryTickModifierHook,
        OnAttackedModifierHook,
        ModifyDamageModifierHook,
        ModifierRemovalHook,
        BlockBreakModifierHook,
        EntityInteractionModifierHook,
        ToolStatsModifierHook,
        BreakSpeedModifierHook,
        ToolDamageModifierHook,
        KeybindInteractModifierHook,
        TooltipModifierHook,
        RequirementsModifierHook,
        ValidateModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this,
                ModifierHooks.ATTRIBUTES,
                ModifierHooks.PROCESS_LOOT,
                ModifierHooks.MELEE_DAMAGE,
                ModifierHooks.MELEE_HIT,
                ModifierHooks.DAMAGE_DEALT,
                ModifierHooks.BOW_AMMO,
                ModifierHooks.PROJECTILE_HIT,
                ModifierHooks.PROJECTILE_LAUNCH,
                ModifierHooks.EQUIPMENT_CHANGE,
                ModifierHooks.INVENTORY_TICK,
                ModifierHooks.ON_ATTACKED,
                ModifierHooks.MODIFY_DAMAGE,
                ModifierHooks.TOOLTIP,
                ModifierHooks.REMOVE,
                ModifierHooks.BLOCK_BREAK,
                ModifierHooks.ENTITY_INTERACT,
                ModifierHooks.TOOL_STATS,
                ModifierHooks.ARMOR_INTERACT,
                ModifierHooks.TOOL_DAMAGE,
                ModifierHooks.REQUIREMENTS
        );
    }

    /**
     * 构造函数 - 优化：移除事件总线注册，改为子类按需注册
     */
    public BaseModifier() {
        // 不再在基类中注册事件监听器，避免所有修饰符都监听相同事件
    }

    /**
     * 可选：子类可覆盖此方法来注册特定的事件监听器
     */
    protected void registerEventListeners() {
        // 默认不注册，子类按需覆盖
    }

    // ============================================
    // Forge事件钩子（供子类覆盖）
    // ============================================

    public void onLivingHurt(LivingHurtEvent event) {}

    public void onLivingAttack(LivingAttackEvent event) {}

    public void onLivingDamage(LivingDamageEvent event) {}

    // ============================================
    // 显示相关
    // ============================================

    /**
     * 是否为无等级修饰符
     */
    public boolean isNoLevels() {
        return false;
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        return isNoLevels() ? super.getDisplayName() : super.getDisplayName(level);
    }

    // ============================================
    // 属性修饰符钩子
    // ============================================

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot,
                              BiConsumer<Attribute, AttributeModifier> consumer) {
        // 默认不添加属性
    }

    // ============================================
    // 战利品处理钩子
    // ============================================

    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> list, LootContext context) {
        // 默认不处理战利品
    }

    // ============================================
    // 近战伤害钩子（优化：减少方法调用层级）
    // ============================================

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                float baseDamage, float damage) {
        // 优化：仅在服务器端且有活体目标时处理
        if (context.getAttacker().level().isClientSide() || context.getLivingTarget() == null) {
            return damage;
        }
        return onModifyMeleeDamage(tool, modifier, context, context.getAttacker(),
                context.getLivingTarget(), baseDamage, damage);
    }

    /**
     * 修改近战伤害（服务器端）
     */
    protected float onModifyMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                        LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        return actualDamage;
    }

    // ============================================
    // 近战命中钩子
    // ============================================

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getAttacker().level().isClientSide() || context.getLivingTarget() == null) {
            return;
        }
        onAfterMeleeHit(tool, modifier, context, context.getAttacker(), context.getLivingTarget(), damageDealt);
    }

    protected void onAfterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                   LivingEntity attacker, LivingEntity target, float damageDealt) {
        // 默认无操作
    }

    @Override
    public void failedMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageAttempted) {
        if (context.getAttacker().level().isClientSide() || context.getLivingTarget() == null) {
            return;
        }
        onFailedMeleeHit(tool, modifier.getLevel(), context, context.getAttacker(),
                context.getLivingTarget(), damageAttempted);
    }

    protected void onFailedMeleeHit(IToolStackView tool, int level, ToolAttackContext context,
                                    LivingEntity attacker, LivingEntity target, float damageAttempted) {
        // 默认无操作
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                float damage, float baseKnockback, float knockback) {
        if (context.getAttacker().level().isClientSide() || context.getLivingTarget() == null) {
            return knockback;
        }
        return onBeforeMeleeHit(tool, modifier.getLevel(), context, context.getAttacker(),
                context.getLivingTarget(), damage, baseKnockback, knockback);
    }

    protected float onBeforeMeleeHit(IToolStackView tool, int level, ToolAttackContext context,
                                     LivingEntity attacker, LivingEntity target, float damage,
                                     float baseKnockback, float knockback) {
        return knockback;
    }

    // ============================================
    // 伤害处理钩子
    // ============================================

    @Override
    public void onDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context,
                              EquipmentSlot slotType, LivingEntity entity, DamageSource damageSource,
                              float amount, boolean isDirectDamage) {
        onModifierDamageDealt(tool, modifier, context, slotType, entity, damageSource, amount, isDirectDamage);
    }

    protected void onModifierDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context,
                                         EquipmentSlot slotType, LivingEntity entity, DamageSource damageSource,
                                         float amount, boolean isDirectDamage) {
        // 默认无操作
    }

    // ============================================
    // 弓箭弹药钩子
    // ============================================

    @Override
    public ItemStack findAmmo(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity,
                              ItemStack itemStack, Predicate<ItemStack> predicate) {
        return modifierFindAmmo(tool, modifiers, livingEntity, itemStack, predicate);
    }

    protected ItemStack modifierFindAmmo(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity,
                                         ItemStack itemStack, Predicate<ItemStack> predicate) {
        return itemStack;
    }

    @Override
    public void shrinkAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter,
                           ItemStack ammo, int needed) {
        modifierShrinkAmmo(tool, modifier, shooter, ammo, needed);
    }

    protected void modifierShrinkAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter,
                                      ItemStack ammo, int needed) {
        // 默认无操作
    }

    // ============================================
    // 抛射物钩子
    // ============================================

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier,
                                         Projectile projectile, EntityHitResult hit,
                                         @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (target == null || attacker == null || attacker.level().isClientSide() ||
                !(projectile instanceof AbstractArrow arrow)) {
            return false;
        }
        onProjectileHitTarget(modifiers, persistentData, modifier.getLevel(), projectile, arrow, hit, attacker, target);
        return false;
    }

    protected void onProjectileHitTarget(ModifierNBT modifiers, ModDataNBT persistentData, int level,
                                         Projectile projectile, AbstractArrow arrow, EntityHitResult hit,
                                         LivingEntity attacker, LivingEntity target) {
        // 默认无操作
    }

    @Override
    public void onProjectileHitBlock(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier,
                                     Projectile projectile, BlockHitResult hit, @Nullable LivingEntity attacker) {
        if (attacker == null || attacker.level().isClientSide() || !(projectile instanceof AbstractArrow)) {
            return;
        }
        modifierOnProjectileHitBlock(modifiers, persistentData, modifier, projectile, hit, attacker);
    }

    protected void modifierOnProjectileHitBlock(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier,
                                                Projectile projectile, BlockHitResult hit, LivingEntity attacker) {
        // 默认无操作
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter,
                                   Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT modDataNBT, boolean primary) {
        if (arrow != null) {
            onProjectileShoot(tool, modifier, shooter, projectile, arrow, modDataNBT, primary);
        }
    }

    protected void onProjectileShoot(IToolStackView bow, ModifierEntry modifier, LivingEntity shooter,
                                     Projectile projectile, AbstractArrow arrow, ModDataNBT modDataNBT, boolean primary) {
        // 默认无操作
    }

    // ============================================
    // 装备变化钩子
    // ============================================

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        modifierOnEquip(tool, modifier, context);
    }

    protected void modifierOnEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        // 默认无操作
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        modifierOnUnequip(tool, modifier, context);
    }

    protected void modifierOnUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        // 默认无操作
    }

    // ============================================
    // 物品栏更新钩子
    // ============================================

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity,
                                int index, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        modifierOnInventoryTick(tool, modifier, level, entity, index, isSelected, isCorrectSlot, itemStack);
    }

    protected void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level,
                                           LivingEntity holder, int itemSlot, boolean isSelected,
                                           boolean isCorrectSlot, ItemStack itemStack) {
        // 默认无操作
    }

    // ============================================
    // 护甲防护钩子
    // ============================================

    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context,
                           EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        modifierOnAttacked(tool, modifier, context, slotType, source, amount, isDirectDamage);
    }

    protected void modifierOnAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context,
                                      EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        // 默认无操作
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context,
                                   EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        return onModifyTakeDamage(tool, modifier, context, slotType, source, amount, isDirectDamage);
    }

    protected float onModifyTakeDamage(IToolStackView tool, ModifierEntry modifier, EquipmentContext context,
                                       EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        return amount;
    }

    // ============================================
    // 修饰符移除钩子
    // ============================================

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        onModifierRemoved(tool);
        return null;
    }

    protected void onModifierRemoved(IToolStackView tool) {
        // 默认无操作
    }

    // ============================================
    // 方块破坏钩子
    // ============================================

    @Override
    public void afterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
        modifierAfterBlockBreak(tool, modifier, context);
    }

    protected void modifierAfterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
        // 默认无操作
    }

    // ============================================
    // 实体交互钩子
    // ============================================

    @Override
    public InteractionResult beforeEntityUse(IToolStackView tool, ModifierEntry modifier, Player player,
                                             Entity target, InteractionHand hand, InteractionSource source) {
        return modifierBeforeEntityUse(tool, modifier, player, target, hand, source);
    }

    protected InteractionResult modifierBeforeEntityUse(IToolStackView tool, ModifierEntry modifier, Player player,
                                                        Entity target, InteractionHand hand, InteractionSource source) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult afterEntityUse(IToolStackView tool, ModifierEntry modifier, Player player,
                                            LivingEntity target, InteractionHand hand, InteractionSource source) {
        return modifierAfterEntityUse(tool, modifier, player, target, hand, source);
    }

    protected InteractionResult modifierAfterEntityUse(IToolStackView tool, ModifierEntry modifier, Player player,
                                                       LivingEntity target, InteractionHand hand, InteractionSource source) {
        return InteractionResult.PASS;
    }

    // ============================================
    // 挖掘速度钩子
    // ============================================

    @Override
    public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, PlayerEvent.BreakSpeed event,
                             Direction sideHit, boolean isEffective, float miningSpeedModifier) {
        modifierBreakSpeed(tool, modifier, event, sideHit, isEffective, miningSpeedModifier);
    }

    protected void modifierBreakSpeed(IToolStackView tool, ModifierEntry modifier, PlayerEvent.BreakSpeed event,
                                      Direction sideHit, boolean isEffective, float miningSpeedModifier) {
        // 默认无操作
    }

    // ============================================
    // 工具属性钩子
    // ============================================

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        modifierAddToolStats(context, modifier, builder);
    }

    protected void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        // 默认无操作
    }

    // ============================================
    // 辅助方法
    // ============================================

    // 移除未使用的抽象方法，避免子类必须实现


    // ============================================
    // 工具提示钩子
    // ============================================

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player,
                           List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        // 默认不添加提示
    }

    // ============================================
    // 按键交互钩子
    // ============================================

    @Override
    public boolean startInteract(IToolStackView tool, ModifierEntry modifier, Player player,
                                 EquipmentSlot slot, TooltipKey keyModifier) {
        return KeybindInteractModifierHook.super.startInteract(tool, modifier, player, slot, keyModifier);
    }

    // ============================================
    // 工具损坏钩子
    // ============================================

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        return modifierDamageTool(tool, modifier, amount, holder);
    }

    protected int modifierDamageTool(IToolStackView tool, ModifierEntry modifier, int amount,
                                     @Nullable LivingEntity holder) {
        return amount;
    }

    // ============================================
    // 需求显示钩子
    // ============================================

    @Override
    public List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of();
    }

    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return null;
    }

    // ============================================
    // 验证钩子
    // ============================================

    @Nullable
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        return null;
    }

    public void afterAttack(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
            LivingEntity attacker, LivingEntity target, boolean wasHit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afterAttack'");
    }
}