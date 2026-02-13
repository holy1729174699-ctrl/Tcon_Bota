package com.holyicey.TconBota.init;

import com.holyicey.TconBota.TconBota;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import java.util.function.Consumer;

public class FluidInit {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, TconBota.MODID);

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, TconBota.MODID);

    // Dark Penis Fluid
    public static final RegistryObject<FluidType> MOLTEN_DARK_PENIS_TYPE = registerFluidType("molten_darkpenis",
            new Vector3f(26f / 255f, 26f / 255f, 26f / 255f), 1000); // Dark Gray

    public static final RegistryObject<FlowingFluid> MOLTEN_DARK_PENIS_SOURCE = FLUIDS.register("molten_darkpenis",
            () -> new ForgeFlowingFluid.Source(FluidInit.MOLTEN_DARK_PENIS_PROPERTIES));

    public static final RegistryObject<FlowingFluid> MOLTEN_DARK_PENIS_FLOWING = FLUIDS.register("molten_darkpenis_flowing",
            () -> new ForgeFlowingFluid.Flowing(FluidInit.MOLTEN_DARK_PENIS_PROPERTIES));

    public static final ForgeFlowingFluid.Properties MOLTEN_DARK_PENIS_PROPERTIES = new ForgeFlowingFluid.Properties(
            MOLTEN_DARK_PENIS_TYPE, MOLTEN_DARK_PENIS_SOURCE, MOLTEN_DARK_PENIS_FLOWING)
            .block(BlockInit.MOLTEN_DARK_PENIS_BLOCK);


    // Mana Penis Fluid
    public static final RegistryObject<FluidType> MOLTEN_MANA_PENIS_TYPE = registerFluidType("molten_manapenis",
            new Vector3f(0f / 255f, 170f / 255f, 221f / 255f), 1200); // Cyan Blue

    public static final RegistryObject<FlowingFluid> MOLTEN_MANA_PENIS_SOURCE = FLUIDS.register("molten_manapenis",
            () -> new ForgeFlowingFluid.Source(FluidInit.MOLTEN_MANA_PENIS_PROPERTIES));

    public static final RegistryObject<FlowingFluid> MOLTEN_MANA_PENIS_FLOWING = FLUIDS.register("molten_manapenis_flowing",
            () -> new ForgeFlowingFluid.Flowing(FluidInit.MOLTEN_MANA_PENIS_PROPERTIES));

    public static final ForgeFlowingFluid.Properties MOLTEN_MANA_PENIS_PROPERTIES = new ForgeFlowingFluid.Properties(
            MOLTEN_MANA_PENIS_TYPE, MOLTEN_MANA_PENIS_SOURCE, MOLTEN_MANA_PENIS_FLOWING)
            .block(BlockInit.MOLTEN_MANA_PENIS_BLOCK);


    private static RegistryObject<FluidType> registerFluidType(String name, Vector3f color, int temperature) {
        ResourceLocation still = new ResourceLocation(TconBota.MODID, "block/fluid/" + name + "/still");
        ResourceLocation flow = new ResourceLocation(TconBota.MODID, "block/fluid/" + name + "/flowing");

        return FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create()
                .density(2000)
                .viscosity(10000)
                .temperature(temperature)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .rarity(Rarity.RARE)) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    @Override
                    public ResourceLocation getStillTexture() {
                        return still;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return flow;
                    }

                    @Override
                    public int getTintColor() {
                        int r = (int) (color.x * 255);
                        int g = (int) (color.y * 255);
                        int b = (int) (color.z * 255);
                        return 0xFF000000 | (r << 16) | (g << 8) | b;
                    }
                });
            }
        });
    }
}
