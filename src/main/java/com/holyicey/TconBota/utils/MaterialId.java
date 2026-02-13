package com.holyicey.TconBota.utils;

import com.holyicey.TconBota.TconBota;



import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;


public class MaterialId {


    public MaterialId(ResourceLocation resourceLocation) {
    }

    public static MaterialId createMaterial(String name) {
        return new MaterialId(new ResourceLocation(TconBota.MODID, name));

    }
}