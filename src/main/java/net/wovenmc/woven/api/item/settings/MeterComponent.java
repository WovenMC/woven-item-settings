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

package net.wovenmc.woven.api.item.settings;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class MeterComponent {
	private final Function<ItemStack, Float> levelFunc;
	private final BiFunction<ItemStack, Float, Integer> colorFunc;
	private final boolean displayAtFull;

	private MeterComponent(Function<ItemStack, Float> levelFunc, BiFunction<ItemStack, Float, Integer> colorFunc,
						   boolean displayAtFull) {
		this.levelFunc = levelFunc;
		this.colorFunc = colorFunc;
		this.displayAtFull = displayAtFull;
	}

	public float getLevel(ItemStack stack) {
		return levelFunc.apply(stack);
	}

	public int getColor(ItemStack stack) {
		return colorFunc.apply(stack, levelFunc.apply(stack));
	}

	public boolean displayAtFull() {
		return displayAtFull;
	}

	public static class Builder {
		private Function<ItemStack, Float> levelFunc = stack ->
				(stack.getMaxDamage() - stack.getDamage()) / (float)stack.getMaxDamage();
		private BiFunction<ItemStack, Float, Integer> colorFunc = (stack, level) ->
				MathHelper.hsvToRgb(levelFunc.apply(stack) / 3F, 1F, 1F);
		private boolean displayAtFull = false;

		public Builder levelFunction(Function<ItemStack, Float> function) {
			this.levelFunc = function;
			return this;
		}

		public Builder colorFunction(BiFunction<ItemStack, Float, Integer> function) {
			this.colorFunc = function;
			return this;
		}

		public Builder displayAtFull() {
			this.displayAtFull = true;
			return this;
		}

		public MeterComponent build() {
			return new MeterComponent(levelFunc, colorFunc, displayAtFull);
		}
	}
}
