package net.guizhanss.guizhanlib.libraries;

import java.io.File;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.bukkit.plugin.Plugin;

import net.byteflux.libby.LibraryManager;
import net.byteflux.libby.classloader.URLClassLoaderHelper;
import net.byteflux.libby.logging.adapters.JDKLogAdapter;

/**
 * A custom {@link LibraryManager} for Bukkit, that loads libraries from the server's root directory.
 * <p>
 * Repositories are tried in insertion order, so a mirror added before Maven Central is preferred.
 * Example:
 * <pre>{@code
 * var manager = new BukkitLibraryManager(plugin);
 * // Optional: mirrors, tried in order
 * manager.addRepository("https://maven.aliyun.com/repository/public/");
 * manager.addRepository("https://maven-central.storage-download.googleapis.com/maven2/");
 * // Last resort: Maven Central is for build-time dependencies, heavy direct
 * // usage hits its rate limits; jars are cached locally after the first download
 * manager.addMavenCentral();
 *
 * manager.loadLibrary(Library.builder()
 *         .groupId("com.google.code.gson")
 *         .artifactId("gson")
 *         .version("2.10.1")
 *         .build());
 * }</pre>
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

    @Override
    protected void addToClasspath(Path file) {
        this.classLoader.addToClasspath(file);
    }
}
