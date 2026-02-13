package com.holyicey.TconBota.data.providers;

import com.holyicey.TconBota.TconBota;
import com.holyicey.TconBota.data.enums.EnumMaterial;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class TconBotaLangProvider extends LanguageProvider {
    public TconBotaLangProvider(PackOutput packOutput) {
        super(packOutput, TconBota.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        // Material Names
        add("material.tconbota.darkpenis", "黑暗那啥");
        add("material.tconbota.manapenis", "魔力那啥");

        // Material Flavor (Description in tooltips/book)
        add("material.tconbota.darkpenis.flavor", "来自黑暗的深邃力量？");
        add("material.tconbota.manapenis.flavor", "充盈着泰拉凝聚的魔力，闪耀着”神秘“的光辉");

        // Encyclopedia Description (Book)
        add("material.tconbota.darkpenis.encyclopedia", "这种材料由黑暗物质凝聚而成，拥有强大的坚硬度和特殊的性质。");
        add("material.tconbota.manapenis.encyclopedia", "通过泰拉凝聚板将魔力注入其中，使其获得了自我修复和魔力传导的能力。");

        // Modifiers
        add("modifier.tconbota.jm", "汲魔");
        add("modifier.tconbota.jm.flavor", "神秘的魔力力量。");
        add("modifier.tconbota.jm.description", "将造成的伤害转换为魔力。");
        add("modifier.tconbota.jm.mana_desc", "魔力转换");
        
        add("modifier.tconbota.foreskin", "包皮");
        add("modifier.tconbota.foreskin.flavor", "这层皮似乎很有韧性。");
        add("modifier.tconbota.foreskin.description", "提供额外的保护。");

        add("modifier.tconbota.hardening", "硬化");
        add("modifier.tconbota.hardening.flavor", "变得更硬了。");
        add("modifier.tconbota.hardening.description", "增加护甲。");

        add("modifier.tconbota.mana_blast", "魔力冲击");
        add("modifier.tconbota.mana_blast.flavor", "冲击！");
        add("modifier.tconbota.mana_blast.description", "攻击时造成1%的额外伤害。");
    }
}
