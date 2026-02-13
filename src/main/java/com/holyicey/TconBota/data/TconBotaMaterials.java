package com.holyicey.TconBota.data;

import com.holyicey.TconBota.TconBota;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class TconBotaMaterials {
    public static final MaterialId darkpenis = create("darkpenis");
    public static final MaterialId manapenis = create("manapenis");

    private static MaterialId create(String name) {
        return new MaterialId(TconBota.MODID, name);
    }
}
