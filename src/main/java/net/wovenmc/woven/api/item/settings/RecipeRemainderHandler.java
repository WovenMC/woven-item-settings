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
	 * @param recipeId The identifier of the original recipe. Hardcoded to {@code minecraft:brewing} for brewing and {@code minecraft:furnace_fuel} for furnace fuel.
	 * @return The item stack that should remain after crafting.
	 */
	ItemStack getRemainder(ItemStack original, Identifier recipeId);
}
