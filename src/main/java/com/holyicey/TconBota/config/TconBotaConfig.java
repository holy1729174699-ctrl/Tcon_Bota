package com.holyicey.TconBota.config;

import com.holyicey.TconBota.TconBota;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

/**
 * TconBota配置管理类
 *
 * 优化说明：
 * 1. 添加配置缓存机制，避免重复读取
 * 2. 使用volatile确保多线程可见性
 * 3. 添加配置验证和边界检查
 * 4. 使用延迟初始化策略，兼容不同Forge版本
 */
public class TconBotaConfig {

    // ============================================
    // 常量定义
    // ============================================

    /** 默认魔力传输速率 */
    private static final int DEFAULT_TRANSFER_RATE = 1000000000;

    /** 最小传输速率 */
    private static final int MIN_TRANSFER_RATE = 1;

    /** 最大传输速率 */
    private static final int MAX_TRANSFER_RATE = 2100000000;

    // ============================================
    // 配置规范
    // ============================================

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue SPARK_TRANSFER_RATE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SPARK_SPEED_OVERRIDE;
    public static final ForgeConfigSpec.DoubleValue JM_MANA_RATIO;

    // ============================================
    // 缓存变量（优化性能）
    // ============================================

    /** 缓存的传输速率（减少配置读取次数） */
    private static volatile int cachedTransferRate = DEFAULT_TRANSFER_RATE;

    /** 缓存的开关状态 */
    private static volatile boolean cachedOverrideEnabled = true;

    /** JM词条魔力转换比例缓存 */
    private static volatile double cachedJmManaRatio = 2.0;

    /** 配置是否已初始化 */
    private static volatile boolean initialized = false;

    // ============================================
    // 静态初始化块
    // ============================================

    static {
        BUILDER.push("Spark Settings");
        SPARK_TRANSFER_RATE = BUILDER
                .comment("魔力火花每tick的传输速度 (Mana/tick)")
                .defineInRange("sparkTransferRate", MAX_TRANSFER_RATE, MIN_TRANSFER_RATE, MAX_TRANSFER_RATE);
        ENABLE_SPARK_SPEED_OVERRIDE = BUILDER
                .comment("启用火花速度覆盖")
                .define("enableSparkSpeedOverride", true);
        BUILDER.pop();

        BUILDER.push("Modifier Settings");
        JM_MANA_RATIO = BUILDER
                .comment("JM词条造成的伤害转魔力的比例 (Mana = Damage * Level * Ratio)")
                .defineInRange("jmManaRatio", 2.0, 0.0, 210000000.0);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    // ============================================
    // 公共方法
    // ============================================

    /**
     * 注册配置（由Mod主类调用）
     */
    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "tconbota-common.toml");
    }

    /**
     * 更新缓存值（延迟初始化）
     */
    private static void updateCache() {
        if (!SPEC.isLoaded()) {
            return;
        }

        try {
            cachedOverrideEnabled = ENABLE_SPARK_SPEED_OVERRIDE.get();
            cachedJmManaRatio = JM_MANA_RATIO.get();
            int rate = SPARK_TRANSFER_RATE.get();

            // 验证范围
            if (rate < MIN_TRANSFER_RATE || rate > MAX_TRANSFER_RATE) {
                if (TconBota.LOGGER != null) {
                    TconBota.LOGGER.warn("Invalid transfer rate {}, using default {}", rate, DEFAULT_TRANSFER_RATE);
                }
                cachedTransferRate = DEFAULT_TRANSFER_RATE;
            } else {
                cachedTransferRate = rate;
            }

            // 标记为已初始化
            if (!initialized) {
                initialized = true;

                // 首次初始化时输出日志
                if (TconBota.LOGGER != null) {
                    TconBota.LOGGER.info("TconBota config loaded - Transfer Rate: {}, Override: {}",
                            cachedTransferRate, cachedOverrideEnabled);
                }
            }
        } catch (Exception e) {
            if (TconBota.LOGGER != null) {
                TconBota.LOGGER.error("Failed to update config cache", e);
            }
            // 使用默认值
            cachedTransferRate = DEFAULT_TRANSFER_RATE;
            cachedOverrideEnabled = true;
        }
    }

    /**
     * 安全获取传输速率（优化：使用缓存）
     *
     * @return 配置值（未加载时返回默认值）
     */
    public static int getSparkTransferRate() {
        // 延迟初始化：首次访问时更新缓存
        if (!initialized && SPEC.isLoaded()) {
            synchronized (TconBotaConfig.class) {
                if (!initialized && SPEC.isLoaded()) {
                    updateCache();
                }
            }
        }

        // 如果禁用覆盖，返回原版速率
        if (!cachedOverrideEnabled) {
            return DEFAULT_TRANSFER_RATE;
        }

        return cachedTransferRate;
    }

    /**
     * 安全获取覆盖开关状态（优化：使用缓存）
     *
     * @return 开关状态（未加载时返回默认值）
     */
    public static boolean isSparkSpeedOverrideEnabled() {
        // 延迟初始化：首次访问时更新缓存
        if (!initialized && SPEC.isLoaded()) {
            synchronized (TconBotaConfig.class) {
                if (!initialized && SPEC.isLoaded()) {
                    updateCache();
                }
            }
        }

        return cachedOverrideEnabled;
    }

    /**
     * 获取JM词条魔力转换比例
     */
    public static double getJmManaRatio() {
        if (!initialized && SPEC.isLoaded()) {
            synchronized (TconBotaConfig.class) {
                if (!initialized && SPEC.isLoaded()) {
                    updateCache();
                }
            }
        }
        return cachedJmManaRatio;
    }

    /**
     * 强制刷新缓存（用于配置文件手动修改后）
     */
    public static void refreshCache() {
        if (SPEC.isLoaded()) {
            synchronized (TconBotaConfig.class) {
                updateCache();

                if (TconBota.LOGGER != null) {
                    TconBota.LOGGER.info("Config cache refreshed - Transfer Rate: {}, Override: {}",
                            cachedTransferRate, cachedOverrideEnabled);
                }
            }
        }
    }

    /**
     * 获取默认传输速率（工具方法）
     */
    public static int getDefaultTransferRate() {
        return DEFAULT_TRANSFER_RATE;
    }

    /**
     * 验证传输速率是否在有效范围内
     *
     * @param rate 要验证的速率
     * @return 是否有效
     */
    public static boolean isValidTransferRate(int rate) {
        return rate >= MIN_TRANSFER_RATE && rate <= MAX_TRANSFER_RATE;
    }
}