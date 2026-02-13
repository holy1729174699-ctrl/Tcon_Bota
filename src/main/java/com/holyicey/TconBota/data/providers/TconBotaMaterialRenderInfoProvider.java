package com.holyicey.TconBota.data.providers;

import com.holyicey.TconBota.data.enums.EnumMaterial;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;

import net.minecraftforge.common.data.ExistingFileHelper;

public class TconBotaMaterialRenderInfoProvider extends AbstractMaterialRenderInfoProvider {
    public TconBotaMaterialRenderInfoProvider(PackOutput packOutput, AbstractMaterialSpriteProvider spriteProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, spriteProvider, existingFileHelper);
    }

    @Override
    protected void addMaterialRenderInfo() {
        for (EnumMaterial material : EnumMaterial.values()) {
            buildRenderInfo(material.id).color(material.color).fallbacks("metal");
        }
    }

    @Override
    public String getName() {
        return "TconBota Material Render Info Provider";
    }
}
