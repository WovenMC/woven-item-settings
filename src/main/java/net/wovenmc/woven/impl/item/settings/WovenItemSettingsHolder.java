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

package net.wovenmc.woven.impl.item.settings;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.wovenmc.woven.api.item.settings.MeterComponent;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public interface WovenItemSettingsHolder {
	@Nullable
	MeterComponent woven$getMeterComponent();

	@Nullable
	BiFunction<ItemStack, Identifier, ItemStack> woven$getDynamicRecipeRemainder();

	@Nullable
	Function<ItemStack, EquipmentSlot> woven$getEquipmentHandler();
}
