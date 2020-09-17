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

import java.util.function.BiFunction;
import java.util.function.Function;

import net.wovenmc.woven.impl.item.settings.WovenItemSettingsHolder;
import net.wovenmc.woven.api.item.settings.MeterComponent;
import net.wovenmc.woven.api.item.settings.WovenItemSettings;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Mixin(Item.class)
public abstract class MixinItem implements WovenItemSettingsHolder {
	@Unique
	private MeterComponent woven$meterComponent;
	@Unique
	private BiFunction<ItemStack, Identifier, ItemStack> woven$dynamicRecipeRemainder;
	@Unique
	private Function<ItemStack, EquipmentSlot> woven$equipmentHandler;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void captureInit(Item.Settings settings, CallbackInfo info) {
		if (settings instanceof WovenItemSettings) {
			WovenItemSettings woven = (WovenItemSettings) settings;
			this.woven$meterComponent = woven.getMeterComponent();
			this.woven$dynamicRecipeRemainder = woven.getDynamicRecipeRemainder();
			this.woven$equipmentHandler = woven.getEquipmentHandler();
		}
	}

	@Nullable
	@Override
	public MeterComponent woven$getMeterComponent() {
		return woven$meterComponent;
	}

	@Nullable
	@Override
	public BiFunction<ItemStack, Identifier, ItemStack> woven$getDynamicRecipeRemainder() {
		return woven$dynamicRecipeRemainder;
	}

	@Nullable
	@Override
	public Function<ItemStack, EquipmentSlot> woven$getEquipmentHandler() {
		return woven$equipmentHandler;
	}
}
