package com.holyicey.TconBota.data.enums;

import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.tools.stats.*;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats.Builder;
import net.minecraft.world.item.Tiers;
import java.util.List;
import java.util.Arrays;

public enum EnumMaterialStats {
    darkpenis(
            armor(300, 1.0f, 2.0f, 1.0f, 0.0f).toughness(1.0f).knockbackResistance(0.1f),
            true,
            new HeadMaterialStats(1500, 8.0f, Tiers.NETHERITE, 6.0f),
            new HandleMaterialStats(1.1f, -50, 1.1f, 1.0f),
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE
    ),
    manapenis(
            armor(1000, 3.0f, 5.0f, 3.0f, 1.0f).toughness(3.0f).knockbackResistance(0.2f),
            true,
            new HeadMaterialStats(3000, 12.0f, Tiers.NETHERITE, 10.0f),
            new HandleMaterialStats(1.3f, 200, 1.2f, 1.2f),
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE
    );

    private final IMaterialStats[] stats;
    private final Builder armorStatBuilder;
    public final boolean allowShield;

    EnumMaterialStats(Builder builder, boolean allowShield, IMaterialStats... stats) {
        this.stats = stats;
        this.armorStatBuilder = builder;
        this.allowShield = allowShield;
    }

    EnumMaterialStats(IMaterialStats... stats) {
        this.stats = stats;
        this.armorStatBuilder = null;
        this.allowShield = false;
    }

    public IMaterialStats[] getStats() {
        return stats;
    }

    public Builder getArmorBuilder() {
        return armorStatBuilder;
    }

    public static Builder armor(int durabilityFactor, float helmet, float chestplate, float leggings, float boots) {
        return PlatingMaterialStats.builder().durabilityFactor(durabilityFactor).armor(boots, leggings, chestplate, helmet);
    }
}
