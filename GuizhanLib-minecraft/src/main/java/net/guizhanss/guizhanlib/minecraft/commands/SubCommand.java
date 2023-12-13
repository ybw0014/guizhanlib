package net.guizhanss.guizhanlib.minecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class SubCommand extends AbstractCommand {
    public SubCommand(@Nullable SubCommand parent, @Nonnull String name, @Nonnull Function<AbstractCommand, String> description
        , @Nonnull String usage, @Nonnull AbstractCommand... subCommands) {
        super(parent, name, description, usage, subCommands);
    }

    @ParametersAreNonnullByDefault
    public SubCommand(String name, Function<AbstractCommand, String> description, String usage, AbstractCommand... subCommands) {
        super(name, description, usage, subCommands);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }

    protected void sendHelp(CommandSender sender) {
        if (hasSubCommands()) {
            for (var subCommand : getSubCommands()) {
                subCommand.sendHelp(sender);
            }
        } else {
            final Deque<AbstractCommand> layers = new ArrayDeque<>();
            AbstractCommand current = this;
            while (current != null) {
                layers.push(current);
                current = current.getParent();
            }
            String cmd = layers.stream().map(AbstractCommand::getName).collect(Collectors.joining(" ", "/", ""));
            String description = getDescription().apply(this);
            sender.sendMessage(ChatColor.YELLOW + cmd + ChatColor.WHITE + " - " + description);
        }
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {

    }

    @Override
    public void onTab(CommandSender sender, String[] args) {

    }
}
