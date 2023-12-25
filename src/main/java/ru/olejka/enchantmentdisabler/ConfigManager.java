package ru.olejka.enchantmentdisabler;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
	private static FileConfiguration config = null;
	private static final List<String> enchants = new ArrayList<>();

	public static void parseConfig(FileConfiguration fileConfiguration) {
		config = fileConfiguration;

		var list = fileConfiguration.getList("enchants");
		if (list == null || list.isEmpty()) return;

		list.stream().map(Object::toString).forEach(enchants::add);
	}

	public static boolean isEnchantDisabled(Enchantment en) {
		return enchants.contains(en.getKey().getKey());
	};

	public static String getMessage(String key) {
		if (config == null) return null;
		return config.getString("messages." + key);
	}
}
