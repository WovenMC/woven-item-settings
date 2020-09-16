package net.wovenmc.woven.mixin.item.settings;

import net.wovenmc.woven.api.item.settings.WovenSettingsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BookCloningRecipe;
import net.minecraft.util.collection.DefaultedList;

@Mixin(BookCloningRecipe.class)
public abstract class MixinBookCloningRecipe {
	private final ThreadLocal<Integer> slot = new ThreadLocal<>();

	@Inject(method = "getRemainingStacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void cacheSlot(CraftingInventory inv, CallbackInfoReturnable<DefaultedList<ItemStack>> info, DefaultedList<ItemStack> ret,
						   int index) {
		slot.set(index);
	}

	@Redirect(method = "getRemainingStacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"))
	private Item getNewRemainder(Item origItem, Item origReturn, CraftingInventory inv) {
		WovenSettingsHolder holder = (WovenSettingsHolder) origItem;
		if (holder.getDynamicRecipeRemainder() != null) {
			return holder.getDynamicRecipeRemainder().apply(inv.getStack(slot.get()));
		}
		return origReturn;
	}
}
