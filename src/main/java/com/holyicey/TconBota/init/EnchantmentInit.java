package com.holyicey.TconBota.init;

import com.holyicey.TconBota.TconBota;
import com.holyicey.TconBota.enchantments.KnowledgeEnchantment;
import com.holyicey.TconBota.enchantments.ScavengerEnchantment;
import com.holyicey.TconBota.enchantments.SoulHarvestEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, TconBota.MODID);

    public static final RegistryObject<Enchantment> SOUL_HARVEST =
            ENCHANTMENTS.register("soul_harvest", SoulHarvestEnchantment::new);

    public static final RegistryObject<Enchantment> KNOWLEDGE =
            ENCHANTMENTS.register("knowledge", KnowledgeEnchantment::new);

    public static final RegistryObject<Enchantment> SCAVENGER =
            ENCHANTMENTS.register("scavenger", ScavengerEnchantment::new);
}
