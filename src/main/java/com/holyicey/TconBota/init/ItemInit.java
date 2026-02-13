package com.holyicey.TconBota.init;

import com.holyicey.TconBota.TconBota;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 物品注册类
 * 集中管理所有模组物品的注册
 */
public class ItemInit {
    
    // 创建 DeferredRegister 实例
    public static final DeferredRegister<Item> ITEMS = 
            DeferredRegister.create(ForgeRegistries.ITEMS, TconBota.MODID);

    // 注册 DarkPenis 物品
    // 使用默认的 Item.Properties，未归类到特定创造模式选项卡（如有需要可后续添加）
    public static final RegistryObject<Item> DARK_PENIS = ITEMS.register("darkpenis",
            () -> new Item(new Item.Properties()));

    // 注册 ManaPenis 物品
    public static final RegistryObject<Item> MANA_PENIS = ITEMS.register("manapenis",
            () -> new Item(new Item.Properties()));
}
