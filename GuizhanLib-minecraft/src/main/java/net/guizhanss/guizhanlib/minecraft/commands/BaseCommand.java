package net.guizhanss.guizhanlib.minecraft.commands;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * The {@link BaseCommand} is the root node of a command tree.
 * It has no parent node.
 */
@Getter
public abstract class BaseCommand extends AbstractCommand implements CommandExecutor, TabCompleter {

    private final Command command;

    @ParametersAreNonnullByDefault
    public BaseCommand(Command command, Function<AbstractCommand, String> description, String usage, AbstractCommand... subCommands) {
        super(command.getName(), description, usage, subCommands);
        this.command = command;
    }

    @ParametersAreNonnullByDefault
    protected void sendHelp(CommandSender sender) {
        if (!hasSubCommands()) {
            sender.sendMessage(getUsage().get());
        } else {
            for (var subCommand : getSubCommands()) {
                subCommand.sendHelp(sender);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!hasSubCommands()) {
            if (getUsage().isValid(args)) {
                onExecute(sender, args);
            } else {
                sendHelp(sender);
            }
        } else {
            if (args.length == 0) {
                sendHelp(sender);
            } else {
                for (var subCommand : getSubCommands()) {
                    if (subCommand.getName().equalsIgnoreCase(args[0])) {
                        subCommand.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                        return true;
                    }
                }
                sendHelp(sender);
            }
        }
        return true;
    }

    @Override
    @Nullable
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
