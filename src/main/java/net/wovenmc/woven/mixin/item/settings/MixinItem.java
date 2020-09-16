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

package net.wovenmc.woven.mixin.item.settings;

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
