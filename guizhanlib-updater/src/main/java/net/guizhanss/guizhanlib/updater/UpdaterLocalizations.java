package net.guizhanss.guizhanlib.updater;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Hard-coded localization.
 * <p>
 * I'm lazy to fix the resource stream shit.
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings("ConstantConditions")
class UpdaterLocalizations {

    private static final Map<String, Map<LocaleKey, String>> MAP = new HashMap<>();

    private static final Map<LocaleKey, String> LOCALE_EN = new EnumMap<>(LocaleKey.class);
    private static final Map<LocaleKey, String> LOCALE_ZH_CN = new EnumMap<>(LocaleKey.class);

    static {
        LOCALE_EN.put(LocaleKey.CANNOT_FIND_REPO, "Cannot find repository in Guizhan Builds. Did you set up updater " +
            "correctly? Is it down?");
        LOCALE_EN.put(LocaleKey.CANNOT_FETCH_INFO, "Cannot fetch information from Guizhan Builds. Is it down?");
        LOCALE_EN.put(LocaleKey.INVALID_VERSION, "Cannot fetch version format from Guizhan Builds. Is it down?");
        LOCALE_EN.put(LocaleKey.INVALID_FILE_VERSION, "Auto updater has been shut down since this version is not from Guizhan Builds.");
        LOCALE_EN.put(LocaleKey.CANNOT_FIND_BUILDS, "Cannot find successful builds. How did you get this plugin?");
        LOCALE_EN.put(LocaleKey.CANNOT_CALCULATE_CHECKSUM, "Cannot calculate the checksum of this plugin.");
        LOCALE_EN.put(LocaleKey.UP_TO_DATE, "{0} is up to date.");
        LOCALE_EN.put(LocaleKey.NEED_UPDATE, "{0} needs to be updated.");
        LOCALE_EN.put(LocaleKey.DOWNLOAD_NOTIFICATION, "Downloading has been disabled due to check only mode. You will need to download from Guizhan Builds manually.");
        LOCALE_EN.put(LocaleKey.DOWNLOADING, "Downloading {0} - Build {1}.");
        LOCALE_EN.put(LocaleKey.DOWNLOAD_FAIL, "Failed to download the new version of {0}.");
        LOCALE_EN.put(LocaleKey.UPDATE_INFO_0, "============== Auto update ==============");
        LOCALE_EN.put(LocaleKey.UPDATE_INFO_1, "Downloaded {0} - Build {1}");
        LOCALE_EN.put(LocaleKey.UPDATE_INFO_2, "Restart server to install the update");

        LOCALE_ZH_CN.put(LocaleKey.CANNOT_FIND_REPO, "无法找到仓库信息。你确定设置好了？");
        LOCALE_ZH_CN.put(LocaleKey.CANNOT_FETCH_INFO, "无法从构建站获取信息。");
        LOCALE_ZH_CN.put(LocaleKey.INVALID_VERSION, "无法从构建站获取版本格式信息。");
        LOCALE_ZH_CN.put(LocaleKey.INVALID_FILE_VERSION, "自动更新已禁用，该版本不是从鬼斩构建站下载的。");
        LOCALE_ZH_CN.put(LocaleKey.CANNOT_FIND_BUILDS, "无法找到成功的构建。你是怎么获取这个插件的。");
        LOCALE_ZH_CN.put(LocaleKey.CANNOT_CALCULATE_CHECKSUM, "无法计算插件的校验和。");
        LOCALE_ZH_CN.put(LocaleKey.UP_TO_DATE, "{0} 已是最新版本。");
        LOCALE_ZH_CN.put(LocaleKey.NEED_UPDATE, "{0} 需要更新。");
        LOCALE_ZH_CN.put(LocaleKey.DOWNLOAD_NOTIFICATION, "由于开启了仅通知模式，下载更新已禁用。你需要手动前往鬼斩构建站下载更新。");
        LOCALE_ZH_CN.put(LocaleKey.DOWNLOADING, "正在下载 {0} - 构建 #{1}");
        LOCALE_ZH_CN.put(LocaleKey.DOWNLOAD_FAIL, "下载 {0} 的新版本失败。");
        LOCALE_ZH_CN.put(LocaleKey.UPDATE_INFO_0, "============== 自动更新 ==============");
        LOCALE_ZH_CN.put(LocaleKey.UPDATE_INFO_1, "已下载 {0} - 构建 #{1}");
        LOCALE_ZH_CN.put(LocaleKey.UPDATE_INFO_2, "重启服务器以安装新版本");

        MAP.put("en-US", LOCALE_EN);
        MAP.put("zh-CN", LOCALE_ZH_CN);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static String get(String lang, LocaleKey key) {
        Preconditions.checkArgument(lang != null, "Language cannot be null");
        Preconditions.checkArgument(key != null, "Language key cannot be null");

        Map<LocaleKey, String> localeMap = MAP.get(lang);

        if (localeMap == null) {
            localeMap = MAP.get("en-US");
        }

        return localeMap.get(key);
    }
}
