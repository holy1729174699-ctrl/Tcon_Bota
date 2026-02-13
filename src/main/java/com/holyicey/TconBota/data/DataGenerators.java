package com.holyicey.TconBota.data;

import com.holyicey.TconBota.TconBota;
import com.holyicey.TconBota.data.providers.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TconBota.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            TconBotaMaterialProvider materialProvider = new TconBotaMaterialProvider(packOutput);
            generator.addProvider(event.includeServer(), materialProvider);
            generator.addProvider(event.includeServer(), new TconBotaMaterialStatsProvider(packOutput, materialProvider));
            generator.addProvider(event.includeServer(), new TconBotaMaterialTraitsProvider(packOutput, materialProvider));
            generator.addProvider(event.includeServer(), new TconBotaRecipeProvider(packOutput));
            generator.addProvider(event.includeServer(), new TconBotaModifierProvider(packOutput));
        }

        if (event.includeClient()) {
            TconBotaMaterialSpriteProvider spriteProvider = new TconBotaMaterialSpriteProvider();
            // generator.addProvider(event.includeClient(), spriteProvider); // Disabled due to API issues
            generator.addProvider(event.includeClient(), new TconBotaMaterialRenderInfoProvider(packOutput, spriteProvider, existingFileHelper));
            generator.addProvider(event.includeClient(), new TconBotaLangProvider(packOutput));
        }
    }
}
