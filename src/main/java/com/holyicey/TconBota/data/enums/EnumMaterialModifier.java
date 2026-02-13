package com.holyicey.TconBota.data.enums;

import com.holyicey.TconBota.init.TconBotaModifiers;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public enum EnumMaterialModifier {
    darkpenis_default(null, entry(TconBotaModifiers.foreskin.getId(), 1)),
    darkpenis_armor(MaterialRegistry.ARMOR, entry(TconBotaModifiers.foreskin.getId(), 1), entry(TconBotaModifiers.hardening.getId(), 1)),

    manapenis_default(null, 
            entry(new ModifierId("etstlib", "mana_repair"), 2), 
            entry(new ModifierId("sakuratinker", "mind"), 1), 
            entry(new ModifierId("sakuratinker", "soul"), 1), 
            entry(TconBotaModifiers.manaBlast.getId(), 1));

    public final ModifierEntry[] modifiers;
    public final MaterialStatsId statType;

    EnumMaterialModifier(MaterialStatsId statType, ModifierEntry... modifiers) {
        this.modifiers = modifiers;
        this.statType = statType;
    }

    public static ModifierEntry entry(ModifierId id, int level) {
        return new ModifierEntry(id, level);
    }

    public static ModifierEntry entry(ModifierId id) {
        return new ModifierEntry(id, 1);
    }
}
