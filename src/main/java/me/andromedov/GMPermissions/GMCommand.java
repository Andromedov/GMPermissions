package me.andromedov.GMPermissions;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GMCommand implements BasicCommand {
    private final GMPermissions plugin;

    public GMCommand(GMPermissions plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        if (!sender.hasPermission("gmpermissions.admin")) {
            Component message = GMPermissions.getMiniMessage()
                    .deserialize("<red>You don't have permission to use this command.</red>");
            sender.sendMessage(message);
            return;
        }

        if (args.length == 0) {
            sendHelpMessage(sender);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadPluginConfig();
                Component reloadMsg = GMPermissions.getMiniMessage()
                        .deserialize("<green>Configuration reloaded!</green>");
                sender.sendMessage(reloadMsg);
                break;

            case "debug":
                if (args.length < 2) {
                    boolean currentDebug = plugin.getConfig().getBoolean("debug.enabled");
                    Component debugStatus = GMPermissions.getMiniMessage()
                            .deserialize("<yellow>Debug mode is currently: " +
                                    (currentDebug ? "<green>ON</green>" : "<red>OFF</red>") + "</yellow>");
                    sender.sendMessage(debugStatus);
                    return;
                }

                boolean enableDebug = args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true");
                plugin.getConfig().set("debug.enabled", enableDebug);
                plugin.saveConfig();
                Component debugMsg = GMPermissions.getMiniMessage()
                        .deserialize("<green>Debug mode " + (enableDebug ? "enabled" : "disabled") + "!</green>");
                sender.sendMessage(debugMsg);
                break;

            case "info":
                sendInfoMessage(sender);
                break;

            case "test":
                sendTestMessage(sender);
                break;

            default:
                Component unknownMsg = GMPermissions.getMiniMessage()
                        .deserialize("<red>Unknown subcommand. Use <yellow>/gmperm</yellow> for help.</red>");
                sender.sendMessage(unknownMsg);
                break;
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack source, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 0 || args.length == 1) {
            String[] subcommands = {"reload", "debug", "info", "test"};
            String input = args.length == 0 ? "" : args[0].toLowerCase();

            for (String cmd : subcommands) {
                if (cmd.startsWith(input)) {
                    completions.add(cmd);
                }
            }
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("debug")) {
            String[] debugOptions = {"on", "off"};
            String input = args[1].toLowerCase();

            for (String option : debugOptions) {
                if (option.startsWith(input)) {
                    completions.add(option);
                }
            }
        }

        return completions;
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender.hasPermission("gmpermissions.admin");
    }

    @Override
    public @NotNull String permission() {
        return "gmpermissions.admin";
    }

    private void sendHelpMessage(CommandSender sender) {
        Component[] helpMessages = {
                GMPermissions.getMiniMessage().deserialize("<yellow><bold>GMPermissions Plugin Commands:</bold></yellow>"),
                GMPermissions.getMiniMessage().deserialize("<gray>/gmperm reload</gray> - <white>Reload configuration</white>"),
                GMPermissions.getMiniMessage().deserialize("<gray>/gmperm debug <on|off></gray> - <white>Toggle debug mode</white>"),
                GMPermissions.getMiniMessage().deserialize("<gray>/gmperm info</gray> - <white>Show plugin information</white>"),
                GMPermissions.getMiniMessage().deserialize("<gray>/gmperm test</gray> - <white>Test MiniMessage formatting</white>")
        };

        for (Component message : helpMessages) {
            sender.sendMessage(message);
        }
    }

    private void sendInfoMessage(CommandSender sender) {
        Component[] infoMessages = {
                GMPermissions.getMiniMessage().deserialize(
                        "<gradient:#ff6b6b:#4ecdc4><bold>GMPermissions Plugin</bold></gradient> <gray>v" +
                                plugin.getDescription().getVersion() + "</gray>"
                ),
                GMPermissions.getMiniMessage().deserialize(
                        "<white>Debug enabled:</white> " +
                                (plugin.getConfig().getBoolean("debug.enabled") ? "<green>Yes</green>" : "<red>No</red>")
                ),
                GMPermissions.getMiniMessage().deserialize(
                        "<white>Auto grant base:</white> " +
                                (plugin.getConfig().getBoolean("permissions.autoGrantBasePermission") ? "<green>Yes</green>" : "<red>No</red>")
                ),
                GMPermissions.getMiniMessage().deserialize(
                        "<white>Supported gamemodes:</white> <yellow>survival</yellow>, <yellow>creative</yellow>, <yellow>adventure</yellow>, <yellow>spectator</yellow>"
                ),
                GMPermissions.getMiniMessage().deserialize(
                        "<white>MiniMessage support:</white> <green>Enabled</green> <gray>(Hex colors, gradients, formatting)</gray>"
                )
        };

        for (Component message : infoMessages) {
            sender.sendMessage(message);
        }
    }

    private void sendTestMessage(CommandSender sender) {
        Component[] testMessages = {
                GMPermissions.getMiniMessage().deserialize("<yellow><bold>MiniMessage Formatting Test:</bold></yellow>"),
                GMPermissions.getMiniMessage().deserialize("<red>Red text</red> and <blue>blue text</blue>"),
                GMPermissions.getMiniMessage().deserialize("<#ff5733>Hex color example</#ff5733>"),
                GMPermissions.getMiniMessage().deserialize("<gradient:#ff0000:#00ff00>Gradient text example</gradient>"),
                GMPermissions.getMiniMessage().deserialize("<bold>Bold</bold>, <italic>italic</italic>, <underlined>underlined</underlined>"),
                GMPermissions.getMiniMessage().deserialize("<rainbow>Rainbow text effect!</rainbow>"),
                GMPermissions.getMiniMessage().deserialize("<hover:show_text:'<yellow>Hover tooltip!</yellow>'>Hover over this text</hover>")
        };

        for (Component message : testMessages) {
            sender.sendMessage(message);
        }
    }
}