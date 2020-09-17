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

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

/**
 * A component that displays a colored meter on an item in a GUI, similar to the vanilla damage bar.
 */
public class MeterComponent {
	private final LevelHandler levelHandler;
	private final ColorHandler colorHandler;
	private final boolean displayAtFull;

	private MeterComponent(LevelHandler levelHandler, ColorHandler colorHandler,
			boolean displayAtFull) {
		this.levelHandler = levelHandler;
		this.colorHandler = colorHandler;
		this.displayAtFull = displayAtFull;
	}

	/**
	 * Get the current level of the meter.
	 * @param stack The item stack to get the level for.
	 * @return The current level, as a float between 0 and 1 inclusive.
	 */
	public float getLevel(ItemStack stack) {
		return levelHandler.getLevel(stack);
	}

	/**
	 * Get the current color of the meter.
	 * @param stack The item stack to get the color for.
	 * @return The current color as an RGB value.
	 */
	public int getColor(ItemStack stack) {
		return colorHandler.getColor(stack, levelHandler.getLevel(stack));
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
		private LevelHandler levelHandler = stack ->
				(stack.getMaxDamage() - stack.getDamage()) / (float) stack.getMaxDamage();
		private ColorHandler colorHandler = (stack, level) ->
				MathHelper.hsvToRgb(levelHandler.getLevel(stack) / 3F, 1F, 1F);
		private boolean displayAtFull = false;

		/**
		 * @param handler The handler for getting the current level of a meter.
		 * @return The builder with the handler set.
		 */
		public Builder levelFunction(LevelHandler handler) {
			this.levelHandler = handler;
			return this;
		}

		/**
		 * @param handler The handler for getting the current color of a meter.
		 * @return The builder with the handler set.
		 */
		public Builder colorHandler(ColorHandler handler) {
			this.colorHandler = handler;
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
			return new MeterComponent(levelHandler, colorHandler, displayAtFull);
		}
	}

	/**
	 * An interface for determining the level of a meter.
	 */
	@FunctionalInterface
	public interface LevelHandler {
		/**
		 * @param stack The stack to get the meter level for.
		 * @return The level of the meter, as a float from 0 to 1 inclusive.
		 */
		float getLevel(ItemStack stack);
	}

	/**
	 * An interface for determining the color of a meter.
	 */
	@FunctionalInterface
	public interface ColorHandler {
		/**
		 * @param stack The stack to get the meter color for.
		 * @param value The current level of the meter.
		 * @return The color to use as an RGB value.
		 */
		int getColor(ItemStack stack, float value);
	}
}
