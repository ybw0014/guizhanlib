package net.guizhanss.guizhanlib.rebar.addon;

import io.github.pylonmc.rebar.addon.RebarAddon;
import net.guizhanss.guizhanlib.minecraft.plugin.AbstractJavaPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An abstract {@link RebarAddon} class that contains some utilities.
 * <p>
 * Extend this as your main class to use them. Do not call `registerWithRebar()` as this is already done.
 *
 * @author ybw0014
 */
@ParametersAreNonnullByDefault
@SuppressWarnings({"ConstantConditions", "unused"})
public abstract class AbstractAddon extends AbstractJavaPlugin implements RebarAddon {

    /**
     * Addon constructor.
     */
    protected AbstractAddon() {
        super();
    }

    /**
     * Get an instance of extended class of {@link AbstractAddon}.
     *
     * @param <T> The class that extends {@link AbstractAddon}, which is the real addon main class
     * @return The instance of extended class of {@link AbstractAddon}
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T extends AbstractAddon> T getInstance() {
        return (T) getPlatformInstance();
    }

    /**
     * Register with Rebar.
     */
    @Override
    protected final void startPlatformTasks() {
        registerWithRebar();
    }

    /**
     * Get this addon as a Java plugin.
     *
     * @return this plugin
     */
    @Nonnull
    @Override
    public final JavaPlugin getJavaPlugin() {
        return this;
    }
}
