package net.guizhanss.guizhanlib.updater;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * This class contains config options for {@link GuizhanBuildsUpdater},
 * which is passed as an argument.
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
     * The base URL of Guizhan Builds.
     */
    @Builder.Default
    private String baseUrl = "https://builds.guizhanss.com/";
}
