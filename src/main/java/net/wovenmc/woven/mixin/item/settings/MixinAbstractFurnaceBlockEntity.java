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

import net.wovenmc.woven.api.item.settings.WovenSettingsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity {
	private final ThreadLocal<ItemStack> stack = new ThreadLocal<>();

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void grabLocalStack(CallbackInfo info, boolean isBurning, boolean isCooking, ItemStack cookingStack) {
		stack.set(cookingStack);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"))
	private Item getNewRemainder(Item origItem, Item origReturn) {
		WovenSettingsHolder holder = (WovenSettingsHolder) origItem;
		if (holder.getDynamicRecipeRemainder() != null) {
			return holder.getDynamicRecipeRemainder().apply(stack.get());
		}
		return origReturn;
	}
}
