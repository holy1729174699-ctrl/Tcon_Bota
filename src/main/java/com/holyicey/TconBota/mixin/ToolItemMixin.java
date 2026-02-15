package com.holyicey.TconBota.mixin;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.capability.MultiCapabilityProvider;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.holyicey.TconBota.init.TconBotaModifiers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

/**
 * ToolItem Mixin
 * 让带有evolution修饰符的匠魂工具实现 Draconic Evolution 的模块化系统
 * 
 * 核心功能：
 * 1. 使工具获得模块网格（12x12）
 * 2. 使工具获得能量存储（1,000,000 OP）
 * 3. 显示能量条和模块信息
 * 4. 支持模块强化
 * 
 * 修复历史：
 * 1. 改用Mixin注入方式，避免字节码冲突
 * 2. 适配ModuleHostImpl正确构造参数
 * 3. 适配ModularOPStorage正确构造参数
 * 4. 修复ModuleCategory引用错误
 * 5. 修复initCapabilities类型转换错误
 * 6. 修复模块网络无法打开：正确注册 ModuleHost 和 OPStorage capabilities
 * 7. 修复addCapability参数顺序错误
 * 8. 添加客户端同步：实现getShareTag和readShareTag，使能量条和模块信息可见
 */
@Mixin(value = ModifiableItem.class, remap = false)
public abstract class ToolItemMixin extends Item {

    // ========== 可配置常量 ==========
    @Unique private static final int MODULE_GRID_WIDTH = 12;
    @Unique private static final int MODULE_GRID_HEIGHT = 12;
    @Unique private static final String PROVIDER_NAME = "tcon_evolution_tool";
    @Unique private static final boolean DELETE_INVALID_MODULES = true;
    @Unique private static final long BASE_ENERGY_CAPACITY = 1_000_000;
    @Unique private static final long BASE_ENERGY_TRANSFER = 100_000;

    public ToolItemMixin(Properties properties) {
        super(properties);
    }

    // ========== 核心工具方法 ==========
    
    /**
     * 检查物品是否带有evolution修饰符
     */
    @Unique
    private boolean hasEvolutionModifier(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ModifiableItem)) {
            return false;
        }
        return ToolStack.from(stack).getModifierLevel(TconBotaModifiers.evolution.getId()) > 0;
    }

    /**
     * 创建ModuleHost
     */
    @Unique
    private ModuleHostImpl createEvolutionModuleHost(ItemStack stack) {
        if (!hasEvolutionModifier(stack)) {
            return null;
        }

        ModuleHostImpl host = new ModuleHostImpl(
                TechLevel.CHAOTIC,
                MODULE_GRID_WIDTH,
                MODULE_GRID_HEIGHT,
                PROVIDER_NAME,
                DELETE_INVALID_MODULES
        );

        // 配置支持的模块分类
        host.addCategories(
                ModuleCategory.ENERGY,
                ModuleCategory.MELEE_WEAPON,
                ModuleCategory.MINING_TOOL,
                ModuleCategory.TOOL_AXE,
                ModuleCategory.TOOL_SHOVEL,
                ModuleCategory.TOOL_HOE
        );

        return host;
    }

    /**
     * 创建能量存储
     */
    @Unique
    private ModularOPStorage createEvolutionOPStorage(ItemStack stack, ModuleHostImpl host) {
        if (!hasEvolutionModifier(stack) || host == null) {
            return null;
        }

        ModularOPStorage opStorage = new ModularOPStorage(host, BASE_ENERGY_CAPACITY, BASE_ENERGY_TRANSFER);
        // 允许能量输入输出
        opStorage.setIOMode(true, true);

        return opStorage;
    }

    // ========== Mixin注入方法 ==========

    /**
     * 注入getTechLevel方法
     */
    @Inject(
            method = "getTechLevel()Lcom/brandon3055/brandonscore/api/TechLevel;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private void injectGetTechLevel(CallbackInfoReturnable<TechLevel> cir) {
        ItemStack stack = new ItemStack((Item) (Object) this);
        if (hasEvolutionModifier(stack)) {
            cir.setReturnValue(TechLevel.CHAOTIC);
        }
    }

    /**
     * 注入createHost方法
     */
    @Inject(
            method = "createHost(Lnet/minecraft/world/item/ItemStack;)Lcom/brandon3055/draconicevolution/api/modules/lib/ModuleHostImpl;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private void injectCreateHost(ItemStack stack, CallbackInfoReturnable<ModuleHostImpl> cir) {
        ModuleHostImpl customHost = createEvolutionModuleHost(stack);
        if (customHost != null) {
            cir.setReturnValue(customHost);
        }
    }

    /**
     * 注入createOPStorage方法
     */
    @Inject(
            method = "createOPStorage(Lnet/minecraft/world/item/ItemStack;Lcom/brandon3055/draconicevolution/api/modules/lib/ModuleHostImpl;)Lcom/brandon3055/draconicevolution/api/modules/lib/ModularOPStorage;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private void injectCreateOPStorage(ItemStack stack, ModuleHostImpl host, CallbackInfoReturnable<ModularOPStorage> cir) {
        ModularOPStorage customStorage = createEvolutionOPStorage(stack, host);
        if (customStorage != null) {
            cir.setReturnValue(customStorage);
        }
    }

    /**
     * 注入initCapabilities方法 - 注册模块系统所需的capabilities
     */
    @Inject(
            method = "initCapabilities(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraftforge/common/capabilities/ICapabilityProvider;",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private void injectInitCapabilities(ItemStack stack, @Nullable CompoundTag nbt, CallbackInfoReturnable<ICapabilityProvider> cir) {
        // 仅处理带evolution修饰符的物品
        if (!hasEvolutionModifier(stack)) {
            return; // 让 TConstruct 的原逻辑继续执行
        }

        try {
            // 创建模块系统组件
            ModuleHostImpl host = createEvolutionModuleHost(stack);
            ModularOPStorage opStorage = createEvolutionOPStorage(stack, host);
            
            if (host == null || opStorage == null) {
                System.err.println("[TconBota] Failed to create module host or OP storage for evolution tool");
                return;
            }

            // 创建 MultiCapabilityProvider
            MultiCapabilityProvider provider = new MultiCapabilityProvider();
            
            // 注册 capabilities
            // 参数顺序：(序列化对象, 名称, capability类型)
            provider.addCapability(host, PROVIDER_NAME + "_module_host", DECapabilities.MODULE_HOST_CAPABILITY);
            provider.addCapability(opStorage, PROVIDER_NAME + "_op_storage", DECapabilities.OP_STORAGE);
            
            // 替换返回值并取消原方法执行
            cir.setReturnValue(provider);
            
        } catch (Exception e) {
            System.err.println("[TconBota] Error creating evolution capabilities: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 注入getShareTag方法 - 关键：将capability数据同步到客户端
     * 
     * 这是让能量条和模块信息在客户端可见的关键！
     * DE的GUI需要客户端能访问这些capability数据
     */
    @Inject(
            method = "getShareTag(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/nbt/CompoundTag;",
            at = @At("RETURN"),
            cancellable = true,
            remap = true
    )
    private void injectGetShareTag(ItemStack stack, CallbackInfoReturnable<CompoundTag> cir) {
        // 仅处理带evolution修饰符的物品
        if (!hasEvolutionModifier(stack)) {
            return;
        }

        try {
            // 获取原有的tag（可能为null）
            CompoundTag originalTag = cir.getReturnValue();
            
            // 使用 DECapabilities 提供的工具方法来写入capability数据
            // 这会将 MODULE_HOST 和 OP_STORAGE 数据序列化到 "share_caps" 标签中
            CompoundTag shareTag = DECapabilities.writeToShareTag(stack, originalTag);
            
            // 替换返回值
            cir.setReturnValue(shareTag);
            
        } catch (Exception e) {
            System.err.println("[TconBota] Error in getShareTag: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 注入readShareTag方法 - 关键：从服务器接收capability数据
     * 
     * 这是客户端接收能量和模块数据的关键！
     */
    @Inject(
            method = "readShareTag(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At("HEAD"),
            remap = true
    )
    private void injectReadShareTag(ItemStack stack, @Nullable CompoundTag nbt, CallbackInfo ci) {
        // 仅处理带evolution修饰符的物品
        if (!hasEvolutionModifier(stack)) {
            return;
        }

        try {
            // 使用 DECapabilities 提供的工具方法来读取capability数据
            // 这会从 "share_caps" 标签中反序列化 MODULE_HOST 和 OP_STORAGE 数据
            DECapabilities.readFromShareTag(stack, nbt);
            
        } catch (Exception e) {
            System.err.println("[TconBota] Error in readShareTag: " + e.getMessage());
            e.printStackTrace();
        }
    }
}