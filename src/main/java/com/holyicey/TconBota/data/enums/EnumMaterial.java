package com.holyicey.TconBota.data.enums;

import com.holyicey.TconBota.data.TconBotaMaterials;
import net.minecraftforge.common.crafting.conditions.ICondition;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

import static com.holyicey.TconBota.data.enums.EnumMaterialModifier.*;

public enum EnumMaterial {
    darkpenis(TconBotaMaterials.darkpenis, 3, 0x1A1A1A, true, false, EnumMaterialStats.darkpenis, null, darkpenis_default, darkpenis_armor),
    manapenis(TconBotaMaterials.manapenis, 4, 0x00AADD, true, false, EnumMaterialStats.manapenis, null, manapenis_default);

    public final MaterialId id;
    public final int tier;
    public final int color;
    public final boolean craftable;
    public final boolean hidden;
    public final EnumMaterialStats stats;
    public final EnumMaterialModifier[] modifiers;
    public final ICondition condition;

    EnumMaterial(MaterialId id, int tier, int color, boolean craftable, boolean hidden, EnumMaterialStats stats, ICondition condition, EnumMaterialModifier... modifiers) {
        this.id = id;
        this.tier = tier;
        this.color = color;
        this.craftable = craftable;
        this.hidden = hidden;
        this.stats = stats;
        this.modifiers = modifiers;
        this.condition = condition;
    }
}
