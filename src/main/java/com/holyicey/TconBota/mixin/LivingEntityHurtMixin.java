package com.holyicey.TconBota.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@Mixin(LivingEntity.class)
public class LivingEntityHurtMixin {

    @Unique
    private static final Logger LOGGER = LogManager.getLogger("LivingEntityHurtMixin");

    @Unique
    private static final ThreadLocal<Integer> tconbota$recursionDepth =
            ThreadLocal.withInitial(() -> 0);

    @Unique
    private static final int MAX_RECURSION = 2;

    @Unique
    private static final Set<LivingEntity> tconbota$beingHurt =
            Collections.newSetFromMap(new WeakHashMap<>());

    // -------------------------------------------------------------------------
    // 修复1：防递归 hurt 拦截
    // -------------------------------------------------------------------------

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void preventRecursiveHurt(DamageSource source, float amount,
                                      CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (tconbota$recursionDepth.get() >= MAX_RECURSION) {
            cir.setReturnValue(false);
            return;
        }
        if (tconbota$isOdamaneDamage(source)) {
            cir.setReturnValue(false);
            return;
        }
        if (tconbota$beingHurt.contains(self)) {
            cir.setReturnValue(false);
            return;
        }

        tconbota$recursionDepth.set(tconbota$recursionDepth.get() + 1);
        tconbota$beingHurt.add(self);
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void cleanupHurtMarkers(DamageSource source, float amount,
                                    CallbackInfoReturnable<Boolean> cir) {
        // HEAD 提前 cancel 时状态未修改，无需清理
        if (cir.isCancelled()) return;

        LivingEntity self = (LivingEntity) (Object) this;
        int depth = tconbota$recursionDepth.get();
        if (depth > 0) tconbota$recursionDepth.set(depth - 1);
        if (tconbota$recursionDepth.get() == 0) tconbota$beingHurt.remove(self);
    }

    // -------------------------------------------------------------------------
    // 修复2：ForgeHooks.onLivingHurt 的 LinkageError 兜底
    //
    // method 用反混淆名 "actuallyHurt"，remap=true（默认）让框架自动映射到
    // 生产环境的 SRG 名，开发和生产环境均可正常工作。
    // -------------------------------------------------------------------------

    @Redirect(
            method = "actuallyHurt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/ForgeHooks;onLivingHurt(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)F"
            )
    )
    private float redirectOnLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        try {
            return ForgeHooks.onLivingHurt(entity, source, amount);
        } catch (LinkageError e) {
            LOGGER.warn(
                    "[LivingEntityHurtMixin] onLivingHurt 触发 LinkageError，已拦截。实体: {}, 伤害源: {}, 伤害量: {}",
                    entity.getScoreboardName(), source.getMsgId(), amount, e
            );
            return amount;
        }
    }

    // -------------------------------------------------------------------------
    // 工具方法
    // -------------------------------------------------------------------------

    @Unique
    private static boolean tconbota$isOdamaneDamage(DamageSource source) {
        if (source == null) return false;
        try {
            String id = source.getMsgId();
            if (id == null) return false;
            String lower = id.toLowerCase();
            return lower.contains("odamane")
                    || lower.contains("void")
                    || lower.contains("halo")
                    || lower.contains("revelation");
        } catch (Exception e) {
            return false;
        }
    }
}
