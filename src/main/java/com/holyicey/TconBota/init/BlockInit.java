package com.holyicey.TconBota.init;

import com.holyicey.TconBota.TconBota;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TconBota.MODID);

    public static final RegistryObject<LiquidBlock> MOLTEN_DARK_PENIS_BLOCK = BLOCKS.register("molten_darkpenis",
            () -> new LiquidBlock(FluidInit.MOLTEN_DARK_PENIS_SOURCE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(net.minecraft.world.level.block.SoundType.EMPTY)));

    public static final RegistryObject<LiquidBlock> MOLTEN_MANA_PENIS_BLOCK = BLOCKS.register("molten_manapenis",
            () -> new LiquidBlock(FluidInit.MOLTEN_MANA_PENIS_SOURCE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(net.minecraft.world.level.block.SoundType.EMPTY)));
}
