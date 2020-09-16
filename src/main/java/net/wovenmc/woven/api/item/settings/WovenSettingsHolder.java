package net.wovenmc.woven.api.item.settings;

import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface WovenSettingsHolder {
	@Nullable
	MeterComponent getMeterComponent();

	@Nullable
	Function<ItemStack, Item> getDynamicRecipeRemainder();
}
