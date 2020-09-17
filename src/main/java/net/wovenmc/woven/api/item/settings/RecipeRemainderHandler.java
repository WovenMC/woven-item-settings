package net.wovenmc.woven.api.item.settings;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * An interface for dynamically handling recipe remainders.
 */
@FunctionalInterface
public interface RecipeRemainderHandler {
	/**
	 * @param original The original item stack used in the recipe.
	 * @param recipeId The ID of the original recipe. Hardcoded to minecraft:brewing for brewing and minecraft:furnace_fuel for furnace fuel.
	 * @return The item stack that should remain after crafting.
	 */
	ItemStack getRemainder(ItemStack original, Identifier recipeId);
}
