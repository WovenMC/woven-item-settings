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

import net.wovenmc.woven.mixin.item.settings.MixinItem;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Rarity;

/**
 * An extension to {@link Item.Settings} providing additional hooks for items.
 */
public class WovenItemSettings extends Item.Settings {
	private MeterComponent meterComponent = null;
	private RecipeRemainderHandler dynamicRecipeRemainder = null;
	private EquipmentHandler equipmentHandler = null;
	private boolean selfRemainder = false;

	/**
	 * @param meterComponent The {@link MeterComponent} for this item.
	 * @return The item settings with the component added.
	 */
	public WovenItemSettings meter(MeterComponent meterComponent) {
		this.meterComponent = meterComponent;
		return this;
	}

	/**
	 * Incompatible with {@link WovenItemSettings#selfRemainder} and {@link WovenItemSettings#recipeRemainder(Item)}.
	 * @param remainder A handler for determining the remainder of an item stack when crafting dynamically.
	 * @return The item settings with the handler added.
	 */
	public WovenItemSettings recipeRemainder(RecipeRemainderHandler remainder) {
		if (selfRemainder) {
			throw new RuntimeException("Unable to have dynamic recipe remainder AND self recipe remainder.");
		} else if (((MixinItem.ItemSettingsAccessor) this).getRecipeRemainder() != null) {
			throw new RuntimeException("Unable to have dynamic recipe remainder AND static recipe remainder.");
		} else {
			this.dynamicRecipeRemainder = remainder;
			return this;
		}
	}

	/**
	 * Incompatible with {@link WovenItemSettings#recipeRemainder(RecipeRemainderHandler)} and {@link Item.Settings#recipeRemainder(Item)}.
	 * Flags an item to return itself as a recipe remainder without a dynamic remainder handler.
	 * @return The item settings with the flag set.
	 */
	public WovenItemSettings selfRemainder() {
		if (dynamicRecipeRemainder != null) {
			throw new RuntimeException("Unable to have self recipe remainder AND dynamic recipe remainder.");
		} else if (((MixinItem.ItemSettingsAccessor) this).getRecipeRemainder() != null) {
			throw new RuntimeException("Unable to have self recipe remainder AND static recipe remainder.");
		} else {
			this.selfRemainder = true;
			return this;
		}
	}

	/**
	 * @param equipmentHandler A handler for determining the equipment slot an item stack should go in.
	 * @return The item settings with the handler added.
	 */
	public WovenItemSettings equipmentHandler(EquipmentHandler equipmentHandler) {
		this.equipmentHandler = equipmentHandler;
		return this;
	}

	@Override
	public WovenItemSettings group(ItemGroup group) {
		super.group(group);
		return this;
	}

	@Override
	public WovenItemSettings rarity(Rarity rarity) {
		super.rarity(rarity);
		return this;
	}

	/**
	 * Incompatible with {@link WovenItemSettings#recipeRemainder(RecipeRemainderHandler)} and {@link WovenItemSettings#selfRemainder}.
	 */
	@Override
	public WovenItemSettings recipeRemainder(Item recipeRemainder) {
		if (dynamicRecipeRemainder != null) {
			throw new RuntimeException("Unable to have static recipe remainder AND dynamic recipe remainder.");
		} else if (selfRemainder) {
			throw new RuntimeException("Unable to have static recipe remainder AND self recipe remainder.");
		} else {
			super.recipeRemainder(recipeRemainder);
			return this;
		}
	}

	@Override
	public WovenItemSettings maxDamage(int maxDamage) {
		super.maxDamage(maxDamage);
		return this;
	}

	@Override
	public WovenItemSettings maxDamageIfAbsent(int maxDamage) {
		super.maxDamageIfAbsent(maxDamage);
		return this;
	}

	@Override
	public WovenItemSettings maxCount(int maxCount) {
		super.maxCount(maxCount);
		return this;
	}

	@Override
	public WovenItemSettings food(FoodComponent foodComponent) {
		super.food(foodComponent);
		return this;
	}

	@Override
	public WovenItemSettings fireproof() {
		super.fireproof();
		return this;
	}

	/**
	 * For internal use.
	 * @return The set meter component, or null if none was set.
	 */
	@Nullable
	public MeterComponent getMeterComponent() {
		return meterComponent;
	}

	/**
	 * For internal use.
	 * @return The set dynamic recipe remainder, or null if none was set.
	 */
	@Nullable
	public RecipeRemainderHandler getRecipeRemainder() {
		return dynamicRecipeRemainder;
	}

	/**
	 * For internal use.
	 * @return Whether the self-remainder flag was set.
	 */
	public boolean remainsSelf() {
		return selfRemainder;
	}

	/**
	 * For internal use.
	 * @return The set equipment handler, or null if none was set
	 */
	@Nullable
	public EquipmentHandler getEquipmentHandler() {
		return equipmentHandler;
	}
}
