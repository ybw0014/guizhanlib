package net.guizhanss.guizhanlib.minecraft.commands;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * This is a node in a command tree.
 * A node can have 0-1 parent node and 0-n child nodes.
 */
@SuppressWarnings("ConstantConditions")
@Getter
public abstract class AbstractCommand {
    private final Set<AbstractCommand> subCommands = new HashSet<>();
    private final AbstractCommand parent;
    private final String name;
    private final Function<AbstractCommand, String> description;
    private final Usage usage;

    protected AbstractCommand(@Nullable AbstractCommand parent, @Nonnull String name,
                              @Nonnull Function<AbstractCommand, String> description,
                              @Nonnull String usage, @Nonnull AbstractCommand... subCommands) {
        Preconditions.checkArgument(name != null && !name.isEmpty(), "name cannot be null or empty");
        Preconditions.checkArgument(description != null, "description cannot be null");

        this.parent = parent;
        this.name = name;
        this.description = description;
        this.usage = new Usage(usage);
        this.subCommands.addAll(Set.of(subCommands));
    }

    @ParametersAreNonnullByDefault
    protected AbstractCommand(String name, Function<AbstractCommand, String> description, String usage,
                              AbstractCommand... subCommands) {
        this(null, name, description, usage, subCommands);
    }

    /**
     * Adds a {@link AbstractCommand} to this node.
     *
     * @param subCommand
     *     the {@link AbstractCommand} to add.
     *
     * @return This {@link AbstractCommand} instance, for chaining.
     */
    @Nonnull
    public AbstractCommand addSubCommand(@Nonnull AbstractCommand subCommand) {
        this.subCommands.add(subCommand);
        return this;
    }

    /**
     * Adds several {@link AbstractCommand} to this node.
     *
     * @param subCommands
     *     the array of {@link AbstractCommand} to add.
     *
     * @return This {@link AbstractCommand} instance, for chaining.
     */
    @Nonnull
    public AbstractCommand addSubCommands(@Nonnull AbstractCommand... subCommands) {
        this.subCommands.addAll(Set.of(subCommands));
        return this;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean hasSubCommands() {
        return !subCommands.isEmpty();
    }

    protected final void onCommandExecute(CommandSender sender, Command command, String label, String[] args) {
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
                        subCommand.onCommandExecute(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                        return;
                    }
                }
                sendHelp(sender);
            }
        }
    }

    protected abstract void sendHelp(CommandSender sender);

    // The actual command logic.
    public abstract void onExecute(CommandSender sender, String[] args);

    public abstract void onTab(CommandSender sender, String[] args);
}
