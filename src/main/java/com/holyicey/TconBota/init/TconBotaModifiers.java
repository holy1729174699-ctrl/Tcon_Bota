package com.holyicey.TconBota.init;

import com.holyicey.TconBota.TconBota;
import com.holyicey.TconBota.modifiers.ForeskinModifier;
import com.holyicey.TconBota.modifiers.HardeningModifier;
import com.holyicey.TconBota.modifiers.JMModifier;
import com.holyicey.TconBota.modifiers.ManaBlastModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class TconBotaModifiers {

    // 私有构造，防止实例化
    private TconBotaModifiers() {}
    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(TconBota.MODID);
    public static final StaticModifier<JMModifier> jm;
    public static final StaticModifier<ForeskinModifier> foreskin;
    public static final StaticModifier<HardeningModifier> hardening;
    public static final StaticModifier<ManaBlastModifier> manaBlast;
    
    static {
        // 注册JMModifier，ID为"jm"（可自定义，需小写、无特殊字符）
        jm = MODIFIERS.register("jm", JMModifier::new);
        
        // 注册新修饰符
        foreskin = MODIFIERS.register("foreskin", ForeskinModifier::new);
        hardening = MODIFIERS.register("hardening", HardeningModifier::new);
        manaBlast = MODIFIERS.register("mana_blast", ManaBlastModifier::new);
    }
}
