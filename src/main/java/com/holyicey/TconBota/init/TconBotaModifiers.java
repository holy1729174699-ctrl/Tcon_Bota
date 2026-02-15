package com.holyicey.TconBota.init;

import com.holyicey.TconBota.TconBota;
import com.holyicey.TconBota.modifiers.ForeskinModifier;
import com.holyicey.TconBota.modifiers.HardeningModifier;
import com.holyicey.TconBota.modifiers.JMModifier;
import com.holyicey.TconBota.modifiers.ManaBlastModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

/**
 * 模组修饰符注册类
 * 负责注册所有的匠魂工具修饰符 (Modifiers)
 */
public class TconBotaModifiers {

    // 私有构造，防止实例化
    private TconBotaModifiers() {}

    /**
     * 创建修饰符延迟注册器
     * 用于将修饰符注册到匠魂的注册表中
     */
    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(TconBota.MODID);

    // ============================================
    // 修饰符实例
    // ============================================

    /** 
     * 汲魔 (JM) 修饰符
     * 效果：攻击时吸取魔力 
     */
    public static final StaticModifier<JMModifier> jm;

    /** 
     * 包皮 (Foreskin) 修饰符
     * 效果：提供额外的保护或耐久增强 
     */
    public static final StaticModifier<ForeskinModifier> foreskin;

    /** 
     * 硬化 (Hardening) 修饰符
     * 效果：增加工具的挖掘等级或伤害 
     */
    public static final StaticModifier<HardeningModifier> hardening;

    /** 
     * 魔力爆发 (Mana Blast) 修饰符
     * 效果：释放魔力脉冲攻击敌人 
     */
    public static final StaticModifier<ManaBlastModifier> manaBlast;
    
    static {
        // 注册JMModifier，ID为"jm"
        jm = MODIFIERS.register("jm", JMModifier::new);
        
        // 注册包皮修饰符
        foreskin = MODIFIERS.register("foreskin", ForeskinModifier::new);
        
        // 注册硬化修饰符
        hardening = MODIFIERS.register("hardening", HardeningModifier::new);
        
        // 注册魔力爆发修饰符
        manaBlast = MODIFIERS.register("mana_blast", ManaBlastModifier::new);

        // 注册进化修饰符
        evolution = MODIFIERS.register("evolution", EvolutionModifier::new);
    }
}
