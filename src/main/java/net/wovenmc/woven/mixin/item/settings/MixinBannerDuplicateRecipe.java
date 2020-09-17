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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BannerDuplicateRecipe;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

@Mixin(BannerDuplicateRecipe.class)
public abstract class MixinBannerDuplicateRecipe extends SpecialCraftingRecipe {
	private final ThreadLocal<Integer> slot = new ThreadLocal<>();

	public MixinBannerDuplicateRecipe(Identifier id) {
		super(id);
	}

	@Inject(method = "getRemainingStacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void cacheSlot(CraftingInventory inv, CallbackInfoReturnable<DefaultedList<ItemStack>> info, DefaultedList<ItemStack> ret,
			int index) {
		slot.set(index);
	}

	@Redirect(method = "getRemainingStacks", at = @At(value = "NEW", target = "net/minecraft/item/ItemStack"))
	private ItemStack getNewRemainder(ItemConvertible origItem, CraftingInventory inv) {
		WovenItemSettingsHolder holder = (WovenItemSettingsHolder) origItem;

		if (holder.woven$getDynamicRecipeRemainder() != null) {
			return holder.woven$getDynamicRecipeRemainder().apply(inv.getStack(slot.get()), this.getId());
		}

		return new ItemStack(origItem);
	}
}
