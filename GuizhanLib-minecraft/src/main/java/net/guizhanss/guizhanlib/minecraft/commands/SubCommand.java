package net.guizhanss.guizhanlib.minecraft.commands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

public abstract class SubCommand extends AbstractCommand {
    protected SubCommand(@Nullable SubCommand parent, @Nonnull String name,
                         @Nonnull Function<AbstractCommand, String> description,
                         @Nonnull String usage, @Nonnull AbstractCommand... subCommands) {
        super(parent, name, description, usage, subCommands);
    }

    @ParametersAreNonnullByDefault
    protected SubCommand(String name, Function<AbstractCommand, String> description,
                         String usage, AbstractCommand... subCommands) {
        super(name, description, usage, subCommands);
    }
}
