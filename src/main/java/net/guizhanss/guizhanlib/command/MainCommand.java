package net.guizhanss.guizhanlib.command;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;
import java.util.List;

public class MainCommand implements CommandExecutor {

    private JavaPlugin plugin;
    private List<SubCommand> subCommandList = new LinkedList<>();

    @ParametersAreNonnullByDefault
    public MainCommand(JavaPlugin plugin, String command) {
        Validate.notNull(plugin, "plugin instance cannot be null");
        Validate.notNull(command, "main command cannot be null");

        this.plugin = plugin;
        PluginCommand pluginCommand = plugin.getCommand(command);

        if (pluginCommand == null) {
            throw new IllegalArgumentException("Command '" + command + "' is not registered in plugin.yml");
        }

        pluginCommand.setExecutor(this);
    }


    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }


    public @Nonnull JavaPlugin getPlugin() {
        return plugin;
    }
}
