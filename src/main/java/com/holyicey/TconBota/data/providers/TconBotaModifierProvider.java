package com.holyicey.TconBota.data.providers;

import com.holyicey.TconBota.init.TconBotaModifiers;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;

public class TconBotaModifierProvider extends AbstractModifierProvider {
    public TconBotaModifierProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModifiers() {
        buildModifier(TconBotaModifiers.jm.getId());
        buildModifier(TconBotaModifiers.foreskin.getId());
        buildModifier(TconBotaModifiers.hardening.getId());
        buildModifier(TconBotaModifiers.manaBlast.getId());
    }

    @Override
    public String getName() {
        return "TconBota Modifier Provider";
    }
}
