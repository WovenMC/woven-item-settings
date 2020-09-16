package net.wovenmc.woven.mixin.item.settings;

import net.wovenmc.woven.api.item.settings.WovenSettingsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(BrewingStandBlockEntity.class)
public abstract class MixinBrewingStandBlockEntity {
	private final ThreadLocal<ItemStack> stack = new ThreadLocal<>();

	@Inject(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"),
		locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void cacheStack(CallbackInfo info, ItemStack stack) {
		this.stack.set(stack);
	}

	@Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"))
	private Item getNewRemainder(Item origItem, Item origReturn) {
		WovenSettingsHolder holder = (WovenSettingsHolder) origItem;
		if (holder.getDynamicRecipeRemainder() != null) {
			return holder.getDynamicRecipeRemainder().apply(stack.get());
		}
		return origReturn;
	}
}
