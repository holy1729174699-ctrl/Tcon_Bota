package com.holyicey.TconBota.mixin;

import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.mega.revelationfix.common.odamane.common.BypassHurtMethodDamageEvents")
public class BypassHurtMethodDamageEventsMixin {

    @Inject(method = "isSourceOdamane", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cancelOdamaneCheck(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
