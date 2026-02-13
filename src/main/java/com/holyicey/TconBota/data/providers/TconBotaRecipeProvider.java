package com.holyicey.TconBota.data.providers;

import com.holyicey.TconBota.TconBota;
import com.holyicey.TconBota.data.TconBotaMaterials;
import com.holyicey.TconBota.init.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipeBuilder;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class TconBotaRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public TconBotaRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        // Dark Penis -> Material
        MaterialRecipeBuilder.materialRecipe(TconBotaMaterials.darkpenis)
                .setIngredient(Ingredient.of(ItemInit.DARK_PENIS.get()))
                .setValue(1)
                .setNeeded(1)
                .save(consumer, new ResourceLocation(TconBota.MODID, "material/darkpenis"));

        // Mana Penis -> Material
        MaterialRecipeBuilder.materialRecipe(TconBotaMaterials.manapenis)
                .setIngredient(Ingredient.of(ItemInit.MANA_PENIS.get()))
                .setValue(1)
                .setNeeded(1)
                .save(consumer, new ResourceLocation(TconBota.MODID, "material/manapenis"));
    }
}
