package net.guizhanss.guizhanlib.minecraft.commands;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is a node in a command tree.
 * A node can have 0-1 parent node and 0-n child nodes.
 *
 * @author ybw0014
 */
@SuppressWarnings("ConstantConditions")
@Getter
public abstract class AbstractCommand {
    private final Set<AbstractCommand> subCommands = new HashSet<>();
    private final AbstractCommand parent;
    private final String name;
    private final Function<AbstractCommand, String> description;
    private final Usage usage;

    /**
     * Creates a new {@link AbstractCommand} instance.
     *
     * @param parent
     *     the parent node.
     * @param name
     *     the name of this node.
     * @param description
     *     the description of this node.
     * @param usage
     *     the usage of this node.
     * @param subCommands
     *     the sub-commands of this node.
     */
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

    /**
     * Creates a new {@link AbstractCommand} instance, without a parent node.
     *
     * @param name
     *     the name of this node.
     * @param description
     *     the description of this node.
     * @param usage
     *     the usage of this node.
     * @param subCommands
     *     the sub-commands of this node.
     */
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

    /**
     * Check if this node has a parent node.
     *
     * @return Whether this node has a parent node.
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Check if this node has child nodes.
     *
     * @return Whether this node has child nodes.
     */
    public boolean hasSubCommands() {
        return subCommands.isEmpty();
    }

    /**
     * The general logic of command execution. This does not contain the real logic of the command.
     *
     * @param sender
     *     The {@link CommandSender} of the command.
     * @param args
     *     The arguments of the command.
     */
    @ParametersAreNonnullByDefault
    protected final void onCommandExecute(CommandSender sender, String[] args) {
        if (hasSubCommands()) {
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
                        subCommand.onCommandExecute(sender, Arrays.copyOfRange(args, 1, args.length));
                        return;
                    }
                }
                sendHelp(sender);
            }
        }
    }

    /**
     * The general logic of tab complete execution. This does not contain the real logic of the command.
     *
     * @param sender
     *     The {@link CommandSender} of the command.
     * @param args
     *     The arguments of the command.
     */
    @Nullable
    @ParametersAreNonnullByDefault
    protected final List<String> onTabCompleteExecute(CommandSender sender, String[] args) {
        if (!hasSubCommands()) {
            if (args.length == 1) {
                return getSubCommands().stream().map(AbstractCommand::getName).toList();
            } else {
                for (var subCommand : getSubCommands()) {
                    if (subCommand.getName().equalsIgnoreCase(args[0])) {
                        return subCommand.onTabCompleteExecute(sender, Arrays.copyOfRange(args, 1,
                            args.length));
                    }
                }
                return List.of();
            }
        } else {
            return onTab(sender, args);
        }
    }

    /**
     * Get the full usage of this node.
     * <p>
     * Example: /command subcommand &lt;arg1&gt; [arg2]
     *
     * @return The full usage of this node.
     */
    @Nonnull
    public String getFullUsage() {
        final Deque<AbstractCommand> layers = new ArrayDeque<>();
        AbstractCommand current = this;
        while (current != null) {
            layers.push(current);
            current = current.getParent();
        }
        String cmd = layers.stream()
            .map(AbstractCommand::getName)
            .collect(Collectors.joining(" ", "/", ""));
        cmd += " " + getUsage().get();
        return cmd;
    }

    /**
     * Send the help message of this node to the {@link CommandSender}.
     *
     * @param sender
     *     The {@link CommandSender} to send the help message to.
     */
    @ParametersAreNonnullByDefault
    protected void sendHelp(CommandSender sender) {
        if (hasSubCommands()) {
            for (var subCommand : getSubCommands()) {
                subCommand.sendHelp(sender);
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + getFullUsage() + ChatColor.WHITE + " - " + getDescription().apply(this));
        }
    }

    // The actual command logic.
    @ParametersAreNonnullByDefault
    public abstract void onExecute(CommandSender sender, String[] args);

    // The actual tab complete logic.
    @ParametersAreNonnullByDefault
    public List<String> onTab(CommandSender sender, String[] args) {
        return List.of();
    }
}
