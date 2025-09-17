package me.andromedov.GMPermissions;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class GMPermissions extends JavaPlugin {

    private static GMPermissions instance;
    private static MiniMessage miniMessage;

    @Override
    public void onEnable() {
        instance = this;
        miniMessage = MiniMessage.miniMessage();

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new GMListener(this), this);

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(
                    "gmperms",
                    "GMPermissions plugin commands",
                    new GMCommand(this)
            );
        });

        if (getConfig().getBoolean("debug.enabled", true)) {
            debug("Debug mode: " + getConfig().getBoolean("debug.enabled"));
            debug("MiniMessage support enabled for rich text formatting");
            debug("Auto grant base permission: " + getConfig().getBoolean("permissions.autoGrantBasePermission"));
            debug("Gamemode permissions are now split into specific permissions:");
            debug("- minecraft.command.gamemode.survival");
            debug("- minecraft.command.gamemode.creative");
            debug("- minecraft.command.gamemode.adventure");
            debug("- minecraft.command.gamemode.spectator");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("GMPermissions Plugin has been disabled!");
        instance = null;
        miniMessage = null;
    }

    public static GMPermissions getInstance() {
        return instance;
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public Component parseMessage(String message, String gamemode) {
        String processedMessage = message.replace("%gamemode%", gamemode);
        return miniMessage.deserialize(processedMessage);
    }

    public void debug(String message) {
        if (getConfig().getBoolean("debug.enabled", true)) {
            getLogger().info("[Debug] " + message);
        }
    }

    public void reloadPluginConfig() {
        reloadConfig();
        debug("Configuration reloaded!");
    }
}