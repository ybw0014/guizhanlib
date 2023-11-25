package net.guizhanss.guizhanlib.updater;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Guizhan Builds Updater is responsible to auto-update the plugin from
 * Guizhan Builds (builds.guizhanss.net).
 * <p>
 * This class can be extended to provide mirror URLs.
 *
 * @author ybw0014
 */
@Getter
@SuppressWarnings("ConstantConditions")
public abstract class AbstractGuizhanBuildsUpdater {

    private final Plugin plugin;
    private final File file;
    private final String user;
    private final String repo;
    private final String branch;
    private final UpdaterConfig config;

    @Getter(AccessLevel.NONE)
    private final Logger logger;

    /**
     * This constructor sets up the updater.
     *
     * @param plugin
     *     The {@link Plugin} instance
     * @param file
     *     The {@link File} of plugin
     * @param user
     *     GitHub user
     * @param repo
     *     GitHub repository
     * @param branch
     *     GitHub branch
     */
    @ParametersAreNonnullByDefault
    protected AbstractGuizhanBuildsUpdater(
        Plugin plugin,
        File file,
        String user,
        String repo,
        String branch
    ) {
        this(plugin, file, user, repo, branch, UpdaterConfig.DEFAULT);
    }

    /**
     * This constructor sets up the updater.
     *
     * @param plugin
     *     The {@link Plugin} instance
     * @param file
     *     The {@link File} of plugin
     * @param user
     *     GitHub user
     * @param repo
     *     GitHub repository
     * @param branch
     *     GitHub branch
     * @param updaterConfig
     *     The {@link UpdaterConfig} of updater.
     */
    @ParametersAreNonnullByDefault
    protected AbstractGuizhanBuildsUpdater(
        Plugin plugin,
        File file,
        String user,
        String repo,
        String branch,
        UpdaterConfig updaterConfig
    ) {
        this.plugin = plugin;
        this.file = file;
        this.user = user;
        this.repo = repo;
        this.branch = branch;
        this.config = updaterConfig;

        this.logger = plugin.getLogger();

        prepareUpdateFolder();
    }

    /**
     * This constructor sets up the updater.
     *
     * @param plugin
     *     The {@link Plugin} instance
     * @param file
     *     The {@link File} of plugin
     * @param user
     *     GitHub user
     * @param repo
     *     GitHub repository
     * @param branch
     *     GitHub branch
     * @param checkOnly
     *     Whether to check the version only, without downloading
     *
     * @deprecated in favor of {@link UpdaterConfig}.
     */
    @Deprecated
    @ParametersAreNonnullByDefault
    protected AbstractGuizhanBuildsUpdater(
        Plugin plugin,
        File file,
        String user,
        String repo,
        String branch,
        boolean checkOnly
    ) {
        this(plugin, file, user, repo, branch, UpdaterConfig.builder().checkOnly(checkOnly).build());
    }

    /**
     * This constructor sets up the updater.
     *
     * @param plugin
     *     The {@link Plugin} instance
     * @param file
     *     The {@link File} of plugin
     * @param user
     *     GitHub user
     * @param repo
     *     GitHub repository
     * @param branch
     *     GitHub branch
     * @param checkOnly
     *     Whether to check the version only, without downloading
     * @param lang
     *     The language of updater
     *
     * @deprecated The language option is no longer used, it is configured globally in GuizhanLibPlugin, or depends
     * on the updater implementation.
     */
    @Deprecated
    @ParametersAreNonnullByDefault
    protected AbstractGuizhanBuildsUpdater(
        Plugin plugin,
        File file,
        String user,
        String repo,
        String branch,
        boolean checkOnly,
        String lang
    ) {
        this(plugin, file, user, repo, branch, checkOnly);
    }

    /**
     * Create update folder
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void prepareUpdateFolder() {
        File dir = new File("plugins/" + Bukkit.getUpdateFolder());

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Override this method to specify the builds page's URL.
     * <p>
     * No trailing slash is needed.
     * <p>
     * Example: {@code https://builds.guizhanss.net}
     *
     * @return the URL of builds page
     */
    @Nonnull
    public abstract String getBuildsURL();

    /**
     * Override this method to specify the R2 bucket URL.
     * <p>
     * No trailing slash is needed.
     * <p>
     * Example: {@code https://builds-r2.gzassets.net}
     *
     * @return The R2 bucket URL
     */
    @Nonnull
    public abstract String getR2URL();

    /**
     * Override this method to set the language of updater.
     *
     * @return the language of updater
     */
    @Nonnull
    public abstract String getLanguage();

    /**
     * Run updater task.
     */
    public void start() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new UpdaterTask(this));
    }

    /**
     * Get the URL of repository list file (repos.json).
     *
     * @return the URL of repository list file
     */
    @Nonnull
    public String getReposFileURL() {
        return getBuildsURL() + "/repos.json";
    }

    /**
     * Get the repository key in repos.json.
     *
     * @return the repository key
     */
    @Nonnull
    public String getRepoKey() {
        return MessageFormat.format("{0}/{1}:{2}", user, repo, branch);
    }

    /**
     * Get the URL of builds information file (builds.json).
     *
     * @param directory
     *     Working directory
     *
     * @return the URL of builds information file
     */
    @Nonnull
    public String getBuildsInfo(@Nonnull String directory) {
        return MessageFormat.format("{0}/{1}/builds.json", getR2URL(), directory);
    }

    /**
     * Call the logger of plugin.
     *
     * @param level
     *     log {@link Level}
     * @param message
     *     the message
     * @param args
     *     the arguments
     */
    public void log(Level level, String message, Object... args) {
        logger.log(level, () -> MessageFormat.format(message, args));
    }

    /**
     * Call the logger of plugin.
     *
     * @param level
     *     log {@link Level}
     * @param exception
     *     the {@link Exception}
     * @param message
     *     the message
     * @param args
     *     the arguments
     */
    public void log(Level level, Exception exception, String message, Object... args) {
        logger.log(level, exception, () -> MessageFormat.format(message, args));
    }

    /**
     * Call the logger of plugin.
     *
     * @param level
     *     log {@link Level}
     * @param key
     *     the message key
     * @param args
     *     the arguments
     */
    public void log(Level level, LocaleKey key, Object... args) {
        log(level, getLocalizedString(key), args);
    }

    /**
     * Call the logger of plugin.
     *
     * @param level
     *     log {@link Level}
     * @param exception
     *     the {@link Exception}
     * @param key
     *     the message key
     * @param args
     *     the arguments
     */
    public void log(Level level, Exception exception, LocaleKey key, Object... args) {
        log(level, exception, getLocalizedString(key), args);
    }

    /**
     * Get the URL of the build artifact.
     *
     * @param directory
     *     Working directory
     * @param target
     *     Target filename
     *
     * @return the URL of the build artifact
     */
    @ParametersAreNonnullByDefault
    @Nonnull
    public String getTargetUrl(String directory, String target) {
        return MessageFormat.format("{0}/{1}/{2}", getR2URL(), directory, target);
    }

    /**
     * Get localized {@link String}.
     *
     * @param key
     *     The localization key.
     *
     * @return The localized {@link String}.
     */
    @ParametersAreNonnullByDefault
    @Nonnull
    public String getLocalizedString(LocaleKey key) {
        Preconditions.checkArgument(key != null, "The localization key cannot be null.");

        return UpdaterLocalizations.get(getLanguage(), key);
    }

    /**
     * Get the SHA1 checksum of the file.
     *
     * @return the checksum of the file, null when there is an error.
     */
    @Nullable
    public String getChecksum() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            FileInputStream fis = new FileInputStream(file);
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
            fis.close();
            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | IOException ex) {
            log(Level.SEVERE, ex, LocaleKey.CANNOT_CALCULATE_CHECKSUM);
            return null;
        }
    }
}
