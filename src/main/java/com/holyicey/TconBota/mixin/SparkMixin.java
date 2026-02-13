package com.holyicey.TconBota.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import vazkii.botania.common.entity.ManaSparkEntity;

// ... existing code ...
@Mixin(ManaSparkEntity.class)
public class SparkMixin {

    /**
     * 方案1：直接修改 TRANSFER_RATE 常量值（推荐）
     * 将默认的 1000 改为自定义值（示例：5000）
     */
    @ModifyConstant(
            method = "tick",
            constant = @Constant(intValue = 1000)
    )
    private int modifyTransferRate(int original) {
        return com.holyicey.TconBota.config.TconBotaConfig.getSparkTransferRate();
    }
}