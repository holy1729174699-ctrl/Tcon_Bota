package com.holyicey.TconBota.data.providers;

import com.holyicey.TconBota.data.enums.EnumMaterial;
import com.holyicey.TconBota.data.enums.EnumMaterialModifier;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.tools.data.material.MaterialTraitsDataProvider;

public class TconBotaMaterialTraitsProvider extends MaterialTraitsDataProvider {
    public TconBotaMaterialTraitsProvider(PackOutput packOutput, TconBotaMaterialProvider materialProvider) {
        super(packOutput, materialProvider);
    }

    @Override
    protected void addMaterialTraits() {
        for (EnumMaterial material : EnumMaterial.values()) {
            for (EnumMaterialModifier materialModifier : material.modifiers) {
                if (materialModifier.statType == null) {
                    addDefaultTraits(material.id, materialModifier.modifiers);
                } else {
                    addTraits(material.id, materialModifier.statType, materialModifier.modifiers);
                }
            }
        }
    }

    @Override
    public String getName() {
        return "TconBota Material Traits Provider";
    }
}
