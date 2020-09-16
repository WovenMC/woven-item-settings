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

/**
 * A component that displays a colored meter on an item in a GUI, similar to the vanilla damage bar.
 */
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

	/**
	 * Get the current level of the meter.
	 * @param stack The item stack to get the level for.
	 * @return The current level, as a float between 0 and 1 inclusive.
	 */
	public float getLevel(ItemStack stack) {
		return levelFunc.apply(stack);
	}

	/**
	 * Get the current color of the meter.
	 * @param stack The item stack to get the color for.
	 * @return The current color as an RGB value.
	 */
	public int getColor(ItemStack stack) {
		return colorFunc.apply(stack, levelFunc.apply(stack));
	}

	/**
	 * @return true if the meter should be rendered when the value is 1.
	 */
	public boolean displayAtFull() {
		return displayAtFull;
	}

	/**
	 * A builder for meter components.
	 */
	public static class Builder {
		private Function<ItemStack, Float> levelFunc = stack ->
				(stack.getMaxDamage() - stack.getDamage()) / (float) stack.getMaxDamage();
		private BiFunction<ItemStack, Float, Integer> colorFunc = (stack, level) ->
				MathHelper.hsvToRgb(levelFunc.apply(stack) / 3F, 1F, 1F);
		private boolean displayAtFull = false;

		/**
		 * @param function The function for getting the current level of a meter.
		 * @return The builder with the function set.
		 */
		public Builder levelFunction(Function<ItemStack, Float> function) {
			this.levelFunc = function;
			return this;
		}

		/**
		 * @param function The function for getting the current color of a meter.
		 * @return The builder with the function set.
		 */
		public Builder colorFunction(BiFunction<ItemStack, Float, Integer> function) {
			this.colorFunc = function;
			return this;
		}

		/**
		 * @return The builder with the flag for displaying at full set.
		 */
		public Builder displayAtFull() {
			this.displayAtFull = true;
			return this;
		}

		/**
		 * @return A built meter component.
		 */
		public MeterComponent build() {
			return new MeterComponent(levelFunc, colorFunc, displayAtFull);
		}
	}
}
