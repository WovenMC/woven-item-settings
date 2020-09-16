package net.wovenmc.woven.api.item.settings;

import java.util.IdentityHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class WovenItemSettings extends Item.Settings {
	private MeterComponent meterComponent;
	private Function<ItemStack, Item> dynamicRecipeRemainder;

	public WovenItemSettings meter(MeterComponent meterComponent) {
		this.meterComponent = meterComponent;
		return this;
	}

	public WovenItemSettings dynamicRecipeRemainder(Function<ItemStack, Item> remainder) {
		this.dynamicRecipeRemainder = remainder;
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

	@Nullable
	public MeterComponent getMeterComponent() {
		return meterComponent;
	}

	@Nullable
	public Function<ItemStack, Item> getDynamicRecipeRemainder() {
		return dynamicRecipeRemainder;
	}
}
