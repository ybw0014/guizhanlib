package net.guizhanss.guizhanlib.updater;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * This class contains config options for {@link AbstractGuizhanBuildsUpdater},
 * which is passed as an argument.
 *
 * @author ybw0014
 * @see AbstractGuizhanBuildsUpdater
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(fluent = true)
public final class UpdaterConfig {
    /**
     * Limits the updater to check update only,
     * no file download.
     */
    private boolean checkOnly = false;

    /**
     * Whether the updater checks the version format
     */
    private boolean checkVersionFormat = true;
}
