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

import net.wovenmc.woven.impl.item.settings.WovenItemSettingsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Mixin(BrewingStandBlockEntity.class)
public abstract class MixinBrewingStandBlockEntity {
	private static final Identifier BREWING_ID = new Identifier("brewing");
	private final ThreadLocal<ItemStack> stack = new ThreadLocal<>();

	@Inject(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void cacheStack(CallbackInfo info, ItemStack stack) {
		this.stack.set(stack);
	}

	@Redirect(method = "craft", at = @At(value = "NEW", target = "net/minecraft/item/ItemStack"))
	private ItemStack getNewRemainder(ItemConvertible origItem) {
		WovenItemSettingsHolder holder = (WovenItemSettingsHolder) origItem;

		if (holder.woven$getDynamicRecipeRemainder() != null) {
			return holder.woven$getDynamicRecipeRemainder().getRemainder(stack.get(), BREWING_ID);
		}

		return new ItemStack(origItem);
	}
}
