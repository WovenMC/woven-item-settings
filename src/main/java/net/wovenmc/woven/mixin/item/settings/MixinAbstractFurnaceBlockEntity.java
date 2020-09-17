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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity {
	@Shadow
	protected DefaultedList<ItemStack> inventory;
	private static final Identifier FUEL_ID = new Identifier("furnace_fuel");
	private final ThreadLocal<ItemStack> stack = new ThreadLocal<>();

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void grabLocalStack(CallbackInfo info, boolean isBurning, boolean isCooking, ItemStack cookingStack, Recipe<?> recipe) {
		stack.set(cookingStack);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"))
	private Item hackCustomFuelRemainder(Item origItem) {
		WovenItemSettingsHolder holder = (WovenItemSettingsHolder) origItem;

		if (holder.woven$getDynamicRecipeRemainder() != null) {
			//hack to make the furnace realize it should do dynamic recipe remainder due to mixin not being able to change flow
			//just needs to not be null
			return Items.AIR;
		}

		return origItem.getRecipeRemainder();
	}

	@Redirect(method = "tick", at = @At(value = "NEW", target = "net/minecraft/item/ItemStack"))
	private ItemStack getNewFuelRemainder(ItemConvertible origItem) {
		WovenItemSettingsHolder holder = (WovenItemSettingsHolder) origItem;

		if (holder.woven$getDynamicRecipeRemainder() != null) {
			return holder.woven$getDynamicRecipeRemainder().getRemainder(stack.get(), FUEL_ID);
		}

		return new ItemStack(origItem);
	}

	@Inject(method = "craftRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"),
			cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void injectSmeltRemainder(Recipe<?> recipe, CallbackInfo info, ItemStack inStack) {
		WovenItemSettingsHolder woven = (WovenItemSettingsHolder) inStack.getItem();

		if (woven.woven$getDynamicRecipeRemainder() != null) {
			ItemStack newStack = woven.woven$getDynamicRecipeRemainder().getRemainder(inStack, recipe.getId());

			if (!newStack.isEmpty()) {
				this.inventory.set(0, newStack);
				info.cancel();
			}
		}
	}
}
