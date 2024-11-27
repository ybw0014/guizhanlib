package net.guizhanss.guizhanlib.libraries;

import net.byteflux.libby.LibraryManager;
import net.byteflux.libby.classloader.URLClassLoaderHelper;
import net.byteflux.libby.logging.adapters.JDKLogAdapter;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A custom {@link LibraryManager} for Bukkit, that loads libraries from the server's root directory.
 *
 * @author ybw0014
 * @since 2.2.0
 */
public final class BukkitLibraryManager extends LibraryManager {

    private final URLClassLoaderHelper classLoader;

    public BukkitLibraryManager(@Nonnull Plugin plugin) {
        super(new JDKLogAdapter(Objects.requireNonNull(plugin).getLogger()), new File(".").toPath(), "libraries");
        this.classLoader = new URLClassLoaderHelper((URLClassLoader) plugin.getClass().getClassLoader(), this);
    }

    protected void addToClasspath(Path file) {
        this.classLoader.addToClasspath(file);
    }
}
