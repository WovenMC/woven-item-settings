package net.wovenmc.woven.api.item.settings;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * An interface for handling the equipment slots of non-armor items.
 */
@FunctionalInterface
public interface EquipmentHandler {
	/**
	 * @param stack The stack to equip.
	 * @return The slot the stack should be equipped to.
	 */
	EquipmentSlot getEquipmentSlot(ItemStack stack);
}
