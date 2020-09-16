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

package net.wovenmc.woven.mixin.item.settings.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.wovenmc.woven.api.item.settings.MeterComponent;
import net.wovenmc.woven.api.item.settings.WovenSettingsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

	@Shadow
	protected abstract void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha);

	@Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamaged()Z"), cancellable = true)
	private void injectCustomMeter(TextRenderer textRenderer, ItemStack stack, int x, int y, String label,
								   CallbackInfo info) {
		WovenSettingsHolder holder = (WovenSettingsHolder) stack.getItem();
		if (holder.getMeterComponent() != null) {
			MeterComponent component = holder.getMeterComponent();
			float value = component.getLevel(stack);
			if (value < 1 || (value == 1 && component.displayAtFull())) {
				//draw the bar
				RenderSystem.disableDepthTest();
				RenderSystem.disableTexture();
				RenderSystem.disableAlphaTest();
				RenderSystem.disableBlend();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferBuilder = tessellator.getBuffer();
				int length = Math.round(13F - value * 13F);
				int color = component.getColor(stack);
				this.renderGuiQuad(bufferBuilder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
				this.renderGuiQuad(bufferBuilder, x + 2, y + 13, length, 1, color >> 16 & 255, color >> 8 & 255, color & 255, 255);
				RenderSystem.enableBlend();
				RenderSystem.enableAlphaTest();
				RenderSystem.enableTexture();
				RenderSystem.enableDepthTest();

				//clean-up for skipped code in ItemRenderer
				ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
				float cooldown = clientPlayerEntity == null ? 0.0F : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack.getItem(), MinecraftClient.getInstance().getTickDelta());
				if (cooldown > 0.0F) {
					RenderSystem.disableDepthTest();
					RenderSystem.disableTexture();
					RenderSystem.enableBlend();
					RenderSystem.defaultBlendFunc();
					Tessellator tesselator = Tessellator.getInstance();
					BufferBuilder buffer = tesselator.getBuffer();
					this.renderGuiQuad(buffer, x, y + MathHelper.floor(16.0F * (1.0F - cooldown)), 16, MathHelper.ceil(16.0F * cooldown), 255, 255, 255, 127);
					RenderSystem.enableTexture();
					RenderSystem.enableDepthTest();
				}
				info.cancel();
			}
		}
	}
}
