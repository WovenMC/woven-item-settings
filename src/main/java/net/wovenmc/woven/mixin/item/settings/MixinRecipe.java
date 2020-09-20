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
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

@Mixin(Recipe.class)
public interface MixinRecipe<C extends Inventory> {
	@Shadow
	Identifier getId();

	/**
	 * @author repulica
	 * @reason injections into interfaces dont work rn
	 * TODO: remove once default interface method injection works
	 */
	@Overwrite
	default DefaultedList<ItemStack> getRemainingStacks(C inventory) {
		DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

		for (int i = 0; i < defaultedList.size(); ++i) {
			Item item = inventory.getStack(i).getItem();
			WovenItemSettingsHolder woven = (WovenItemSettingsHolder) item;

			if (woven.woven$getDynamicRecipeRemainder() != null) {
				defaultedList.set(i, woven.woven$getDynamicRecipeRemainder().getRemainder(inventory.getStack(i), getId()));
			} else if (item.hasRecipeRemainder()) {
				defaultedList.set(i, new ItemStack(item.getRecipeRemainder()));
			}
		}

		return defaultedList;
	}

	/*
	@Inject(method = "getRemainingStacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	default void cacheSlot(C inv, CallbackInfoReturnable<?> info, DefaultedList<ItemStack> ret,
			int index) {
		GrossThreadLocalHack.THREADLOCALS.computeIfAbsent((Recipe<?>)this, x -> new ThreadLocal<>()).set(index);
	}

	@Redirect(method = "getRemainingStacks", at = @At(value = "NEW", target = "net/minecraft/item/ItemStack"))
	default ItemStack getNewRemainder(ItemConvertible origItem, C inv) {
		WovenItemSettingsHolder holder = (WovenItemSettingsHolder) origItem;

		if (holder.woven$getDynamicRecipeRemainder() != null) {
			return holder.woven$getDynamicRecipeRemainder().apply(inv.getStack(
					GrossThreadLocalHack.THREADLOCALS.computeIfAbsent(
							(Recipe<?>)this, x -> new ThreadLocal<>()).get()), this.getId());
		}

		return new ItemStack(origItem);
	}*/
}
