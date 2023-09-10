package dev.cammiescorner.hookshot.core.registry;

import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;

public class ModItems {
	//----Item Map----//
	public static final LinkedHashMap<Item, Identifier> ITEMS = new LinkedHashMap<>();

	//-----Items-----//
	public static final Item HOOKSHOT_TOOL = create("hookshot", new HookshotItem());

	//-----Registry-----//
	public static void register() {
		ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
	}

	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, new Identifier(Hookshot.MOD_ID, name));
		return item;
	}
}
