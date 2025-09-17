package me.andromedov.GMPermissions;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.HashMap;
import java.util.Map;

public class GMListener implements Listener {

    private final GMPermissions plugin;

    public GMListener(GMPermissions plugin) {
        this.plugin = plugin;
    }

    private static final Map<String, String> GAMEMODE_PERMISSIONS = new HashMap<>();
    static {
        GAMEMODE_PERMISSIONS.put("survival", "minecraft.command.gamemode.survival");
        GAMEMODE_PERMISSIONS.put("creative", "minecraft.command.gamemode.creative");
        GAMEMODE_PERMISSIONS.put("adventure", "minecraft.command.gamemode.adventure");
        GAMEMODE_PERMISSIONS.put("spectator", "minecraft.command.gamemode.spectator");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        plugin.debug("Player " + player.getName() + " executed command: " + message);

        if (handleGamemodeCommand(player, message)) {
            plugin.debug("Command blocked for player " + player.getName());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onServerCommand(ServerCommandEvent event) {
        CommandSender sender = event.getSender();
        String command = event.getCommand();

        plugin.debug("Server command executed: /" + command);

        if (handleGamemodeCommand(sender, "/" + command)) {
            plugin.debug("Server command blocked");
            event.setCancelled(true);
        }
    }

    private boolean handleGamemodeCommand(CommandSender sender, String message) {
        String[] parts = message.toLowerCase().trim().split("\\s+");
        if (parts.length == 0) return false;

        String command = parts[0].startsWith("/") ? parts[0].substring(1) : parts[0];
        if (!command.equals("gamemode")) return false;

        plugin.debug("Detected gamemode command: " + command);

        if (parts.length < 2) {
            plugin.debug("No gamemode specified, allowing vanilla handling");
            return false;
        }

        String requestedMode = parts[1].toLowerCase();
        String requiredPermission = GAMEMODE_PERMISSIONS.get(requestedMode);

        plugin.debug("Requested mode: " + requestedMode + ", Required permission: " + requiredPermission);

        if (requiredPermission == null) {
            plugin.debug("Invalid gamemode (only full names accepted): " + requestedMode);
            sendFormattedMessage(sender, "invalidGamemode", "");
            return true;
        }

        boolean hasBasePermission = checkBasePermission(sender, requiredPermission);
        boolean hasSpecificPermission = sender.hasPermission(requiredPermission);

        plugin.debug("Base permission check: " + hasBasePermission);
        plugin.debug("Specific permission check: " + hasSpecificPermission);

        if (!hasBasePermission && !hasSpecificPermission) {
            plugin.debug("Sender lacks both base and specific permissions");
            return false;
        }

        if (!hasSpecificPermission) {
            plugin.debug("Sender lacks specific permission: " + requiredPermission);
            sendFormattedMessage(sender, "noPermission", getGamemodeName(requestedMode));
            return true;
        }

        plugin.debug("Permission check passed, allowing command");
        return false;
    }

    private void sendFormattedMessage(CommandSender sender, String messageKey, String gamemode) {
        String messageTemplate = plugin.getConfig().getString("messages." + messageKey, "<red>Error: Message not found</red>");

        Component message = plugin.parseMessage(messageTemplate, gamemode);
        sender.sendMessage(message);
    }

    private boolean checkBasePermission(CommandSender sender, String specificPermission) {
        if (sender.hasPermission("minecraft.command.gamemode")) {
            return true;
        }

        if (plugin.getConfig().getBoolean("permissions.autoGrantBasePermission", true)) {
            if (sender.hasPermission("minecraft.command.gamemode.*")) {
                return true;
            }

            for (String permission : GAMEMODE_PERMISSIONS.values()) {
                if (sender.hasPermission(permission)) {
                    plugin.debug("Auto-granting base permission due to specific permission: " + permission);
                    return true;
                }
            }
        }

        return false;
    }

    private String getGamemodeName(String mode) {
        switch (mode.toLowerCase()) {
            case "survival":
                return "Survival";
            case "creative":
                return "Creative";
            case "adventure":
                return "Adventure";
            case "spectator":
                return "Spectator";
            default:
                return mode;
        }
    }
}