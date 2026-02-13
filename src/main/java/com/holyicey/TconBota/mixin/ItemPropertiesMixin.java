package com.holyicey.TconBota.mixin;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * ItemProperties Mixin
 * <p>
 * 修复多个模组在并行加载阶段同时注册物品属性时导致的 ConcurrentModificationException
 */
@Mixin(ItemProperties.class)
public abstract class ItemPropertiesMixin {

    @Shadow
    @Final
    private static Map<Item, Map<ResourceLocation, ClampedItemPropertyFunction>> GENERIC_PROPERTIES;

    /**
     * @author HolyIcey
     * @reason Fix ConcurrentModificationException when multiple mods register item properties concurrently during client setup
     */
    @Overwrite
    public static void register(Item item, ResourceLocation name, ClampedItemPropertyFunction property) {
        // 同步访问 map，防止多线程竞争导致的崩溃
        synchronized (GENERIC_PROPERTIES) {
            GENERIC_PROPERTIES.computeIfAbsent(item, (k) -> Maps.newHashMap()).put(name, property);
        }
    }
}
