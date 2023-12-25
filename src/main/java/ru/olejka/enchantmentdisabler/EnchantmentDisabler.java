package ru.olejka.enchantmentdisabler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class EnchantmentDisabler extends JavaPlugin {
    private static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigManager.parseConfig(getConfig());
        saveDefaultConfig();

        logger = getLogger();
        Bukkit.getPluginManager().registerEvents(new EventManager(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    static Logger getPluginLogger() {
        return logger;
    }
}
