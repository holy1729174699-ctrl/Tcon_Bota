package com.holyicey.TconBota.utils;

import com.holyicey.TconBota.integration.IntegrationManager;


/**
 * 安全类工具 - 提供模组ID常量和加载状态检查
 *
 * 优化说明：
 * 1. 移除静态初始化的加载检查（改为使用IntegrationManager）
 * 2. 仅保留常量定义
 * 3. 添加废弃标记，引导使用IntegrationManager
 *
 * @deprecated 请使用 {@link IntegrationManager} 代替
 */
@Deprecated
public final class SafeClassUtil {

    /**
     * 模组ID常量类
     */
    public static final class Modid {
        /** Botania模组ID */
        public static final String Botania = "botania";

        /** Tinkers' Calibration模组ID */
        public static final String TinkersCalibration = "tinkerscalibration";

        /** SlashBlade模组ID */
        public static final String SlashBlade = "slashblade";

        /** Tinkers' Construct模组ID */
        public static final String TinkersConstruct = "tconstruct";

        private Modid() {}
    }

    /**
     * 检查Botania是否已加载
     *
     * @deprecated 使用 {@link IntegrationManager#isBotaniaLoaded()}
     */
    @Deprecated
    public static boolean isBotaniaLoaded() {
        return IntegrationManager.isBotaniaLoaded();
    }

    /**
     * 检查Tinkers' Calibration是否已加载
     *
     * @deprecated 使用 {@link IntegrationManager#isTinkersCalibrationLoaded()}
     */
    @Deprecated
    public static boolean isTinkersCalibrationLoaded() {
        return IntegrationManager.isTinkersCalibrationLoaded();
    }

    /**
     * 检查SlashBlade是否已加载
     *
     * @deprecated 使用 {@link IntegrationManager#isSlashBladeLoaded()}
     */
    @Deprecated
    public static boolean isSlashBladeLoaded() {
        return IntegrationManager.isSlashBladeLoaded();
    }

    // 私有构造函数
    private SafeClassUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}