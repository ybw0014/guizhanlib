package net.guizhanss.guizhanlib.updater;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Contains shared configuration options for the library's updaters.
 * Callers supply this configuration directly rather than through a configuration file.
 * Individual updaters use their own platform defaults where applicable.
 *
 * @author ybw0014
 * @see GuizhanBuildsUpdater
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(fluent = true)
public final class UpdaterConfig {

    public static final UpdaterConfig DEFAULT = new UpdaterConfig();

    /**
     * Limits the updater to check update only,
     * no file download.
     */
    @Builder.Default
    private boolean checkOnly = false;

    /**
     * The Guizhan Builds base URL, retained as the legacy default for the deprecated updater.
     * Guizhan Resources resolves its own default when this value is unchanged, while the other
     * platform updaters use their own API origins.
     */
    @Builder.Default
    private String baseUrl = "https://builds.guizhanss.com/";

    /**
     * Optional HTTP User-Agent override. When unset, new updaters use
     * {@code pluginName/pluginVersion (GuizhanLib)} at request time.
     */
    private String userAgent;

    /**
     * Optional bearer token for the new updaters. It is sent only to the platform API origin
     * and is not forwarded to cross-origin download hosts.
     */
    private String apiKey;

    /**
     * Optional Guizhan Resources Minecraft version filter, also checked against declared
     * candidate compatibility when present.
     */
    private String minecraftVersion;

    /**
     * Optional Guizhan Resources platform filter, also checked against declared candidate
     * compatibility when present.
     */
    private String platform;

    /**
     * Creates a configuration using the original two arguments.
     *
     * @param checkOnly whether to notify without downloading
     * @param baseUrl the Guizhan Builds or Guizhan Resources base URL
     */
    public UpdaterConfig(boolean checkOnly, String baseUrl) {
        this.checkOnly = checkOnly;
        this.baseUrl = baseUrl;
    }
}
