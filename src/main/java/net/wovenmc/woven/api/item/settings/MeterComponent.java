package net.wovenmc.woven.api.item.settings;

import java.util.function.BiFunction;
import java.util.function.Function;

import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import org.lwjgl.system.MathUtil;

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
