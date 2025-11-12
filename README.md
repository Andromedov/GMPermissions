# GMPermissions - Gamemode Permission Splitter

[![Paper Version](https://img.shields.io/badge/Paper-1.21.10-blue.svg)](https://papermc.io/)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Plugin Version](https://img.shields.io/badge/Version-1.1.0-brightgreen.svg)](https://github.com/andromedov/gm-permissions/releases)

A lightweight and modern Minecraft Paper plugin that splits the default `minecraft.command.gamemode` permission into specific permissions for each gamemode, providing granular control over player gamemode access. 

Work only on Paper and it's forks

## ✨ Features

- **🎯 Granular Permissions**: Split gamemode permissions into specific permissions for each mode
- **🎨 MiniMessage Support**: Full MiniMessage support with HEX colors, gradients, and formatting
- **🔧 Configurable**: Easy-to-use YAML configuration with camelCase naming
- **🐛 Debug Mode**: Comprehensive debug logging for troubleshooting

## 🔐 Permissions

### Core Permissions

| Permission                             | Description                           | Default  |
|----------------------------------------|---------------------------------------|----------|
| `minecraft.command.gamemode.survival`  | Allows switching to survival mode     | op       |
| `minecraft.command.gamemode.creative`  | Allows switching to creative mode     | op       |
| `minecraft.command.gamemode.adventure` | Allows switching to adventure mode    | op       |
| `minecraft.command.gamemode.spectator` | Allows switching to spectator mode    | op       |
| `minecraft.command.gamemode.*`         | Wildcard permission for all gamemodes | op       |

### Admin Permissions

| Permission                  | Description                              | Default  |
|-----------------------------|------------------------------------------|----------|
| `gamemodepermissions.admin` | Access to plugin administration commands | op       |

### Permission Inheritance

The wildcard permission `minecraft.command.gamemode.*` automatically includes all specific gamemode permissions.

## 📦 Installation

1. **Download** the latest release from the [Releases](https://github.com/Andromedov/GMPermissions/releases) page
2. **Place** the JAR file in your server's `plugins/` directory
3. **Restart** your server
4. **Configure** permissions using your preferred permission plugin (LuckPerms, GroupManager, etc.)

### Requirements

- **Server**: Paper 1.21+ (Tested on 1.21 & 1.21.8)
- **Java**: Java 21 or higher
- **Permission Plugin**: LuckPerms, GroupManager, or any other compatible permission plugin

## ⚙️ Configuration

The plugin generates a `config.yml` file with the following structure:

```yaml
# GMPermissions Configuration

# Debug option (ENABLE ONLY IF YOU KNOW WHAT YOU'RE DOING)
debug:
  enabled: false

# Permission settings
permissions:
  # If true, having any specific gamemode permission automatically grants base permission
  # If false, you should give a "minecraft.command.gamemode" permission for command to work
  autoGrantBasePermission: true

# Custom permission messages using MiniMessage format
messages:
  # Placeholders: %gamemode%
  # Supports hex colors, gradients, formatting, etc.
  noPermission: "<red>You don't have permission to change to <yellow>%gamemode%</yellow> mode.</red>"
  invalidGamemode: "<red>Invalid gamemode. Use: <yellow>survival</yellow>, <yellow>creative</yellow>, <yellow>adventure</yellow>, or <yellow>spectator</yellow></red>"
```

## 📋 Commands

### Player Commands

| Command                | Description               | Permission                             |
|------------------------|---------------------------|----------------------------------------|
| `/gamemode survival`   | Switch to survival mode   | `minecraft.command.gamemode.survival`  |
| `/gamemode creative`   | Switch to creative mode   | `minecraft.command.gamemode.creative`  |
| `/gamemode adventure`  | Switch to adventure mode  | `minecraft.command.gamemode.adventure` |
| `/gamemode spectator`  | Switch to spectator mode  | `minecraft.command.gamemode.spectator` |

### Admin Commands

| Command                   | Description                 | Permission                  |
|---------------------------|-----------------------------|-----------------------------|
| `/gmperm`                 | Show help menu              | `gamemodepermissions.admin` |
| `/gmperm reload`          | Reload plugin configuration | `gamemodepermissions.admin` |
| `/gmperm debug <on\|off>` | Toggle debug mode           | `gamemodepermissions.admin` |
| `/gmperm info`            | Display plugin information  | `gamemodepermissions.admin` |
| `/gmperm test`            | Test MiniMessage formatting | `gamemodepermissions.admin` |

## 🐛 Debug Mode

Enable debug mode to troubleshoot permission issues:

```bash
# Enable debug logging
/gmperm debug on

# Test a gamemode command
/gamemode creative

# Check console for detailed logs:
# [Debug] Player PlayerName executed command: /gamemode creative
# [Debug] Detected gamemode command: gamemode
# [Debug] Requested mode: creative, Required permission: minecraft.command.gamemode.creative
# [Debug] Base permission check: true
# [Debug] Specific permission check: true
# [Debug] Permission check passed, allowing command
```

## 🔧 Building

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Andromedov/GMPermissions.git
   cd GMPermissions
   ```

2. **Build with Maven**:
   ```bash
   mvn clean package
   ```

3. **Find the compiled JAR** in the `target/` directory

### Project Structure

```
GMPermissions/
├── src/main/java/me/andromedov/GMPermissions/
│   ├── GMPermissions.java      # Main plugin class
│   ├── GMListener.java         # Event listener
│   └── GMCommand.java          # Admin commands
├── src/main/resources/
│   ├── config.yml              # Default configuration
│   └── paper-plugin.yml        # Plugin metadata
├── pom.xml                     # Maven configuration
└── README.md                   # This file
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit issues, feature requests, or pull requests.

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues or have questions:

- **GitHub Issues**: [Report a bug or request a feature](https://github.com/Andromedov/GMPermissions/issues)

## 📊 Compatibility

### Tested Versions

- ✅ Paper 1.21 & Paper 1.21.8
- ✅ Java 21
- ✅ LuckPerms 5.5.9

### Known Bugs

- ❌ Versions below 1.21 is not supported (modern API required)
