package net.wovenmc.woven.mixin.item.settings;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.wovenmc.woven.api.item.settings.WovenSettingsHolder;
import net.wovenmc.woven.api.item.settings.MeterComponent;
import net.wovenmc.woven.api.item.settings.WovenItemSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(Item.class)
public abstract class MixinItem implements WovenSettingsHolder {
	private MeterComponent meterComponent;
	private Function<ItemStack, Item> dynamicRecipeRemainder;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void captureInit(Item.Settings settings, CallbackInfo info) {
		if (settings instanceof WovenItemSettings) {
			WovenItemSettings woven = (WovenItemSettings) settings;
			this.meterComponent = woven.getMeterComponent();
			this.dynamicRecipeRemainder = woven.getDynamicRecipeRemainder();
		}
	}

	@Override
	public MeterComponent getMeterComponent() {
		return meterComponent;
	}

	@Override
	public Function<ItemStack, Item> getDynamicRecipeRemainder() {
		return dynamicRecipeRemainder;
	}
}
