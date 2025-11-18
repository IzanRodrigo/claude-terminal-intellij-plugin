# Claude Code Terminal

Forked from [happut](https://github.com/lupishan/cursor-cli-terminal](https://github.com/happut/claude-terminal-intellij-plugin)

A JetBrains IDE plugin that runs `claude` (Claude Code CLI) directly inside your IDE via an integrated terminal window.

### ✨ Features

- **One-click access**: Opens in a dedicated tool window on the right side
- **Auto-start**: Automatically launches `claude` when you open the tool window
- **Seamless integration**: Works like a native IDE terminal
- **Smart detection**: Automatically finds `claude` in your PATH
- **Focus & Restart**: Quick shortcuts to focus or restart Claude Code (hold Shift to clean restart)
- **Privacy-first**: No network calls, no data collection

### 📋 Requirements

- IntelliJ IDEA 2023.2+ (or any JetBrains IDE: PyCharm, WebStorm, PhpStorm, etc.)
- `claude` CLI installed and available in your PATH

### 📦 Installation

**From JetBrains Marketplace:**
1. Open IDE → Settings → Plugins → Marketplace
2. Search for "Claude Code Terminal"
3. Click Install

**From Source:**
1. Clone this repository
2. Run `./gradlew buildPlugin`
3. Install the generated ZIP from `build/distributions/` via Settings → Plugins → Install from Disk

### 🚀 Usage

1. Open the "Claude Code Terminal" tool window from the right sidebar
2. The `claude` CLI will start automatically if detected in your PATH
3. Use **Tools → Focus / Restart Claude Code Terminal** (`Ctrl+Shift+C` on Windows/Linux, `Cmd+Shift+C` on Mac) to:
   - Focus the tool window (normal click)
   - Restart the session (hold Shift while clicking)

### 🛠️ Development

**Build the plugin:**
```bash
./gradlew buildPlugin
```

**Run in IDE sandbox:**
```bash
./gradlew runIde
```

**Project Structure:**
- `src/main/java/com/hamdiwanis/claude/` - Plugin source code
  - `ClaudeToolWindowFactory.java` - Creates the tool window and terminal widget
  - `StartClaudeCodeAction.java` - Handles focus/restart actions
  - `ClaudeCodeUtils.java` - Shared utility methods
- `src/main/resources/` - Plugin resources (icons, plugin.xml)
- `build.gradle` - Build configuration

**Tech Stack:**
- Java 17
- IntelliJ Platform SDK
- Gradle with IntelliJ Plugin

### 🤝 Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

### ❓ FAQ

**Q: "claude not detected" error**
A: Make sure `claude` CLI is installed and in your PATH. You can install it from [Claude Code documentation](https://docs.claude.com/docs/claude-code). Restart the IDE after installation.

**Q: First-time authentication prompts**
A: Follow the interactive prompts in the terminal window to authenticate with your Anthropic account.

**Q: Does this work with other JetBrains IDEs?**
A: Yes! Works with PyCharm, WebStorm, PhpStorm, and all IntelliJ-based IDEs.

### 🔒 Privacy

- No network calls
- No data collection
- All commands execute in the IDE's built-in terminal (fully transparent)

### 📄 License

MIT License - see [LICENSE](LICENSE) file for details.

### ⚠️ Disclaimer

This plugin is an independent project and is **not affiliated with or endorsed by Anthropic** or any other third-party vendors. It does not bundle, download, or install `claude` CLI.
