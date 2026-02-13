package com.holyicey.TconBota;

import com.holyicey.TconBota.config.TconBotaConfig;
import com.holyicey.TconBota.handlers.*;
import com.holyicey.TconBota.init.EnchantmentInit;
import com.holyicey.TconBota.init.ItemInit;
import com.holyicey.TconBota.init.TconBotaModifiers;
import com.holyicey.TconBota.integration.IntegrationManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TconBota模组主类
 *
 * 优化说明：
 * 1. 移除不必要的事件监听器
 * 2. 简化初始化流程
 * 3. 添加模组加载状态日志
 * 4. 优化事件总线注册顺序
 * 5. 移除未使用的导入和方法
 */
@Mod(TconBota.MODID)
public class TconBota {

    // ============================================
    // 常量定义
    // ============================================

    /** 模组ID */
    public static final String MODID = "tconbota";

    /** 日志记录器 */
    public static final Logger LOGGER = LogManager.getLogger();

    // ============================================
    // 构造函数
    // ============================================

    public TconBota() {
        LOGGER.info("TconBota initializing...");

        // 获取事件总线
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册配置（必须在所有其他注册之前）
        TconBotaConfig.register();

        // 初始化集成管理器（必须在其他处理器注册之前）
        IntegrationManager.init(modEventBus);

        // 注册修饰符
        TconBotaModifiers.MODIFIERS.register(modEventBus);
        LOGGER.info("Modifiers registered");

        // 注册附魔
        EnchantmentInit.ENCHANTMENTS.register(modEventBus);
        LOGGER.info("Enchantments registered");

        // 注册物品
        ItemInit.ITEMS.register(modEventBus);
        LOGGER.info("Items registered");

        // 注册方块
        com.holyicey.TconBota.init.BlockInit.BLOCKS.register(modEventBus);
        LOGGER.info("Blocks registered");

        // 注册流体
        com.holyicey.TconBota.init.FluidInit.FLUID_TYPES.register(modEventBus);
        com.holyicey.TconBota.init.FluidInit.FLUIDS.register(modEventBus);
        LOGGER.info("Fluids registered");

        // 注册生命周期事件
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        // 注册Forge事件总线
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new KnowledgeHandler());
        MinecraftForge.EVENT_BUS.register(new ScavengerHandler());
        
        // 注册集成处理器 (按需注册，防止NoClassDefFoundError)
        if (IntegrationManager.isBotaniaLoaded()) {
            MinecraftForge.EVENT_BUS.register(new JMModifierHandler());
            LOGGER.info("Botania integration registered (JMModifier, Dreamwood)");
        }
        
        if (IntegrationManager.isSlashBladeLoaded()) {
            MinecraftForge.EVENT_BUS.register(new SlashBladeHandler());
            LOGGER.info("SlashBlade integration registered");
        }

        LOGGER.info("TconBota initialization complete");
        LOGGER.info("Spark Transfer Rate: {} Mana/tick", TconBotaConfig.getSparkTransferRate());
    }

    // ============================================
    // 生命周期事件
    // ============================================

    /**
     * 通用设置阶段（服务器和客户端都会执行）
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("TconBota common setup");

        // 在这里执行通用设置任务
        event.enqueueWork(() -> {
            // 确保配置已加载
            if (TconBotaConfig.isSparkSpeedOverrideEnabled()) {
                LOGGER.info("Spark speed override enabled with rate: {}",
                        TconBotaConfig.getSparkTransferRate());
            } else {
                LOGGER.info("Spark speed override disabled, using Botania default");
            }
        });
    }

    /**
     * 客户端设置阶段（仅客户端执行）
     */
    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("TconBota client setup");

        // 在这里执行客户端特定的设置
        event.enqueueWork(() -> {
            // 客户端初始化代码（如果需要）
        });
    }
}