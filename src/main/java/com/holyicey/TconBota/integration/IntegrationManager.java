package com.holyicey.TconBota.integration;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.eventbus.api.IEventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.HashMap;

/**
 * 集成管理器 - 管理与其他模组的集成
 *
 * 优化说明：
 * 1. 使用 ConcurrentHashMap 确保线程安全
 * 2. 提供简洁的集成检查方法
 * 3. 统一管理所有集成状态
 */
public final class IntegrationManager {

    private static final Logger LOGGER = LogManager.getLogger();

    // ============================================
    // 模组ID常量
    // ============================================

    public static final String BOTANIA = "botania";
    public static final String TINKERS_CONSTRUCT = "tconstruct";
    public static final String TINKERS_CALIBRATION = "tinkerscalibration";
    public static final String SLASHBLADE = "slashblade";
    public static final String CURIOS = "curios";

    // ============================================
    // 模组加载状态缓存
    // ============================================

    private static final Map<String, Boolean> MOD_LOAD_STATUS = new ConcurrentHashMap<>();
    private static volatile boolean initialized = false;

    private IntegrationManager() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void init(IEventBus modEventBus) {
        if (initialized) return;

        checkModLoadStatus();
        initialized = true;
        LOGGER.info("IntegrationManager initialized. Detected {} active integrations.",
                getLoadedIntegrationsCount());
    }

    private static void checkModLoadStatus() {
        ModList modList = ModList.get();
        MOD_LOAD_STATUS.put(BOTANIA, modList.isLoaded(BOTANIA));
        MOD_LOAD_STATUS.put(TINKERS_CONSTRUCT, modList.isLoaded(TINKERS_CONSTRUCT));
        MOD_LOAD_STATUS.put(TINKERS_CALIBRATION, modList.isLoaded(TINKERS_CALIBRATION));
        MOD_LOAD_STATUS.put(SLASHBLADE, modList.isLoaded(SLASHBLADE));
        MOD_LOAD_STATUS.put(CURIOS, modList.isLoaded(CURIOS));
    }

    // ============================================
    // 公共API方法
    // ============================================

    public static boolean isModLoaded(String modId) {
        if (modId == null || modId.isEmpty()) return false;
        if (!initialized) return ModList.get().isLoaded(modId);
        return MOD_LOAD_STATUS.getOrDefault(modId, false);
    }

    public static boolean isBotaniaLoaded() { return isModLoaded(BOTANIA); }
    public static boolean isTinkersLoaded() { return isModLoaded(TINKERS_CONSTRUCT); }
    public static boolean isTinkersCalibrationLoaded() { return isModLoaded(TINKERS_CALIBRATION); }
    public static boolean isSlashBladeLoaded() { return isModLoaded(SLASHBLADE); }
    public static boolean isCuriosLoaded() { return isModLoaded(CURIOS); }

    /**
     * 安全检查物品是否为SlashBlade
     */
    public static boolean isSlashBladeItem(net.minecraft.world.item.ItemStack stack) {
        if (!isSlashBladeLoaded()) return false;
        return SlashBladeCompat.isSlashBlade(stack);
    }

    public static int getLoadedIntegrationsCount() {
        return (int) MOD_LOAD_STATUS.values().stream().filter(b -> b).count();
    }

    public static Map<String, Boolean> getModLoadStatus() {
        return new HashMap<>(MOD_LOAD_STATUS);
    }

    public static void refresh() {
        LOGGER.info("Refreshing IntegrationManager...");
        MOD_LOAD_STATUS.clear();
        initialized = false;
        checkModLoadStatus();
        initialized = true;
        LOGGER.info("IntegrationManager refreshed");
    }
}
