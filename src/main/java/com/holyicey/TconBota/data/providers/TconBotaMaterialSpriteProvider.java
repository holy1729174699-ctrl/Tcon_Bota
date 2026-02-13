package com.holyicey.TconBota.data.providers;

import com.holyicey.TconBota.data.enums.EnumMaterial;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

public class TconBotaMaterialSpriteProvider extends AbstractMaterialSpriteProvider {

    @Override
    public String getName() {
        return "TconBota Material Sprite Provider";
    }

    @Override
    protected void addAllMaterials() {
        for (EnumMaterial material : EnumMaterial.values()) {
            // TODO: Fix texture generation API
            /*
            buildMaterial(material.id)
                .statType(HeadMaterialStats.ID, Identifier.head)
                .statType(HandleMaterialStats.ID, Identifier.handle)
                .statType(StatlessMaterialStats.BINDING.getIdentifier(), Identifier.extra);
            */
        }
    }
}
