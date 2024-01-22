package net.guizhanss.guizhanlib.minecraft.commands;

import lombok.Getter;
import org.bukkit.command.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Function;

/**
 * The {@link BaseCommand} is the root node of a command tree.
 * It has no parent node.
 *
 * @author ybw0014
 */
@Getter
public abstract class BaseCommand extends AbstractCommand implements CommandExecutor, TabCompleter {

    private final PluginCommand command;

    @ParametersAreNonnullByDefault
    protected BaseCommand(PluginCommand command, Function<AbstractCommand, String> description, String usage,
                          AbstractCommand... subCommands) {
        super(command.getName(), description, usage, subCommands);
        this.command = command;
    }

    @Override
    @ParametersAreNonnullByDefault
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        onCommandExecute(sender, command, label, args);
        return true;
    }

    @Override
    @Nullable
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return onTabCompleteExecute(sender, args);
    }

    /**
     * Register this command.
     */
    public void register() {
        command.setExecutor(this);
        command.setTabCompleter(this);
    }
}
