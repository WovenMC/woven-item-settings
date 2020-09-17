/*
 * Copyright (c) 2020 WovenMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
