package net.guizhanss.guizhanlib.minecraft.commands;

import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;

public abstract class SubCommand extends AbstractCommand {
    protected SubCommand(@Nullable AbstractCommand parent, @Nonnull String name,
                         @Nonnull BiFunction<AbstractCommand, CommandSender, String> description,
                         @Nonnull String usage, @Nonnull AbstractCommand... subCommands) {
        super(parent, name, description, usage, subCommands);
    }

    @ParametersAreNonnullByDefault
    protected SubCommand(String name, BiFunction<AbstractCommand, CommandSender, String> description,
                         String usage, AbstractCommand... subCommands) {
        super(name, description, usage, subCommands);
    }
}
