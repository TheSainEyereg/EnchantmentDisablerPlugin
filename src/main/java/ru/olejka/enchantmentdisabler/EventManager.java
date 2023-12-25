package ru.olejka.enchantmentdisabler;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.logging.Logger;

public class EventManager implements Listener {

//	private static final Logger logger = EnchantmentDisabler.getPluginLogger();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		var player = e.getPlayer();
		var inv = player.getInventory();

		for (var i : inv.getContents()) {
			if (i == null || i.getType().equals(Material.AIR)) continue;

			if (i.getType().equals(Material.ENCHANTED_BOOK)) {
				var em = (EnchantmentStorageMeta) i.getItemMeta();
				if (em == null) continue;

				for (var en : em.getStoredEnchants().keySet()) {
					if(ConfigManager.isEnchantDisabled(en)) {
						inv.remove(i);
					}
				}
			}

			for (var en : i.getEnchantments().keySet()) {
				if (ConfigManager.isEnchantDisabled(en)) {
					i.removeEnchantment(en);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerClickItem(InventoryClickEvent e){

		var player = (Player) e.getWhoClicked();

		var clickedInventory = e.getClickedInventory();
		var currentItem = e.getCurrentItem();
		if (currentItem == null || clickedInventory == null) return;

		var message = ConfigManager.getMessage("restricted");
		if (message == null) message = "This enchantment is restricted.";

		if (currentItem.getType().equals(Material.AIR)) return;

		if (currentItem.getType().equals(Material.ENCHANTED_BOOK)) {
			var em = (EnchantmentStorageMeta) e.getCurrentItem().getItemMeta();
			if (em == null) return;

			for (var en : em.getStoredEnchants().keySet()) {
				if(ConfigManager.isEnchantDisabled(en)) {
					e.setCancelled(true);
					if (clickedInventory.getType().equals(InventoryType.MERCHANT)) {
						player.sendMessage(ChatColor.RED + message);
						return;
					}
					player.getInventory().remove(currentItem);
					return;
				}
			}
		}
		for (var en : e.getCurrentItem().getEnchantments().keySet()){
			if (ConfigManager.isEnchantDisabled(en)) {
				e.setCancelled(true);
				if (clickedInventory.getType().equals(InventoryType.MERCHANT)) {
					player.sendMessage(ChatColor.RED + message);
					continue;
				}
				currentItem.removeEnchantment(en);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPrepEnchant(PrepareItemEnchantEvent e){
		for (var eo : e.getOffers().clone()) {
			if (eo == null) continue;
			var en = eo.getEnchantment();

			if (ConfigManager.isEnchantDisabled(en)) {
				// Удали эту нахуй починки и на "Тут могла быть Удача"
				eo.setEnchantment(Enchantment.MENDING);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerOpenAnInventory(InventoryOpenEvent e){
		for (var i : e.getInventory().getContents()) {
			if (i == null || i.getType().equals(Material.AIR)) continue;

			if (i.getType().equals(Material.ENCHANTED_BOOK)) {
				var em = (EnchantmentStorageMeta) i.getItemMeta();
				if (em == null) continue;

				for (var en : em.getStoredEnchants().keySet()) {
					if(ConfigManager.isEnchantDisabled(en)) {
						e.getInventory().remove(i);
					}
				}
			}

			for (Enchantment en : i.getEnchantments().keySet()) {
				if (ConfigManager.isEnchantDisabled(en)) {
					i.removeEnchantment(en);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemPickUp(EntityPickupItemEvent e){
		var item = e.getItem();
		var i = item.getItemStack();


		if (i.getType().equals(Material.ENCHANTED_BOOK)) {
			var em = (EnchantmentStorageMeta) i.getItemMeta();
			if (em == null) return;

			for (var en : em.getStoredEnchants().keySet()) {
				if(ConfigManager.isEnchantDisabled(en)) {
					e.setCancelled(true);
					item.remove();
				}
			}
		}

		for (Enchantment en : i.getEnchantments().keySet()){
			if(ConfigManager.isEnchantDisabled(en)){
				i.removeEnchantment(en);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemEnchant(EnchantItemEvent e) {

//		Player player = e.getEnchanter();
//		var message = ConfigManager.getMessage("removed");
//		if (message == null) message = "Some enchantment was removed.";

		for(var entry : e.getEnchantsToAdd().entrySet()){
			var key = entry.getKey();
			if(key == null || !ConfigManager.isEnchantDisabled(key)) continue;

			e.getEnchantsToAdd().remove(key, e.getEnchantsToAdd().get(key));

//			player.sendMessage(ChatColor.GOLD + message);
		}
	}
}
