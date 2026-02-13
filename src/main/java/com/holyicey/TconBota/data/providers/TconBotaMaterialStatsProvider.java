package com.holyicey.TconBota.data.providers;

import com.holyicey.TconBota.data.enums.EnumMaterial;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;

public class TconBotaMaterialStatsProvider extends AbstractMaterialStatsDataProvider {
    public TconBotaMaterialStatsProvider(PackOutput packOutput, TconBotaMaterialProvider materialProvider) {
        super(packOutput, materialProvider);
    }

    @Override
    protected void addMaterialStats() {
        for (EnumMaterial material : EnumMaterial.values()) {
            if (material.stats.getArmorBuilder() != null) {
                addArmorStats(material.id, material.stats.getArmorBuilder(), material.stats.getStats());
                if (material.stats.allowShield) {
                    addMaterialStats(material.id, material.stats.getArmorBuilder().buildShield());
                }
            } else {
                addMaterialStats(material.id, material.stats.getStats());
            }
        }
    }

    @Override
    public String getName() {
        return "TconBota Material Stats Provider";
    }
}
