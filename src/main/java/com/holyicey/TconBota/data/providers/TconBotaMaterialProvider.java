package com.holyicey.TconBota.data.providers;

import com.holyicey.TconBota.data.enums.EnumMaterial;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;

public class TconBotaMaterialProvider extends AbstractMaterialDataProvider {
    public TconBotaMaterialProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addMaterials() {
        for (EnumMaterial material : EnumMaterial.values()) {
            // Assuming order is 100 + ordinal to keep them grouped
            addMaterial(material.id, material.tier, 100 + material.ordinal(), material.craftable);
        }
    }

    @Override
    public String getName() {
        return "TconBota Material Data Provider";
    }
}
