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

import java.util.function.BiFunction;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

/**
 * An extension to {@link Item.Settings} providing additional hooks for items.
 */
public class WovenItemSettings extends Item.Settings {
	private MeterComponent meterComponent = null;
	private BiFunction<ItemStack, Identifier, ItemStack> dynamicRecipeRemainder = null;
	private Function<ItemStack, EquipmentSlot> equipmentHandler = null;

	/**
	 * @param meterComponent The {@link MeterComponent} for this item.
	 * @return The item settings with the component added.
	 */
	public WovenItemSettings meter(MeterComponent meterComponent) {
		this.meterComponent = meterComponent;
		return this;
	}

	/**
	 * @param remainder A function for determining the remainder of an item stack when crafting dynamically.
	 * @return The item settings with the function added.
	 */
	public WovenItemSettings dynamicRecipeRemainder(BiFunction<ItemStack, Identifier, ItemStack> remainder) {
		this.dynamicRecipeRemainder = remainder;
		return this;
	}

	/**
	 * @param equipmentHandler A function for determining the equipment slot an item stack should go in.
	 * @return The item settings with the function added.
	 */
	public WovenItemSettings equipmentHandler(Function<ItemStack, EquipmentSlot> equipmentHandler) {
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

	@Override
	public WovenItemSettings recipeRemainder(Item recipeRemainder) {
		super.recipeRemainder(recipeRemainder);
		return this;
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
	public BiFunction<ItemStack, Identifier, ItemStack> getDynamicRecipeRemainder() {
		return dynamicRecipeRemainder;
	}

	/**
	 * For internal use.
	 * @return The set equipment handler, or null if none was set
	 */
	@Nullable
	public Function<ItemStack, EquipmentSlot> getEquipmentHandler() {
		return equipmentHandler;
	}
}
