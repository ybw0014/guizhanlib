package net.guizhanss.guizhanlib.updater;

import com.google.gson.JsonObject;
import net.guizhanss.guizhanlib.utils.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class AbstractGuizhanBuildsUpdater {
    private final Plugin plugin;
    private final File file;
    private final String user;
    private final String repo;
    private final String branch;
    private final boolean checkOnly;
    private String lang;
    private JsonObject locale;

    /**
     * 初始化构建站更新
     *
     * @param user      用户
     * @param repo      仓库
     * @param branch    分支
     * @param checkOnly 是否仅检查而不下载更新
     */
    @ParametersAreNonnullByDefault
    public AbstractGuizhanBuildsUpdater(
        Plugin plugin,
        File file,
        String user,
        String repo,
        String branch,
        boolean checkOnly
    ) {
        this(plugin, file, user, repo, branch, checkOnly, "zh-CN");
    }

    /**
     * 初始化构建站更新 (包含语言)
     *
     * @param user      用户
     * @param repo      仓库
     * @param branch    分支
     * @param checkOnly 是否仅检查而不下载更新
     * @param lang      设置自动更新模块语言
     */
    @ParametersAreNonnullByDefault
    public AbstractGuizhanBuildsUpdater(
        Plugin plugin,
        File file,
        String user,
        String repo,
        String branch,
        boolean checkOnly,
        String lang
    ) {
        this.plugin = plugin;
        this.file = file;
        this.user = user;
        this.repo = repo;
        this.branch = branch;
        this.checkOnly = checkOnly;
        this.lang = lang;

        prepareUpdateFolder();
        loadLang();
    }

    /**
     * 创建下载目录
     */
    private void prepareUpdateFolder() {
        File dir = new File("plugins/" + Bukkit.getUpdateFolder());

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 加载更新模块语言文件
     */
    private void loadLang() {
        // 从资源中加载
        InputStream stream = plugin.getResource("updater.json");
        if (stream == null) {
            throw new IllegalStateException("The updater's language file is missing, did you correctly relocated it?");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        JsonObject langRoot = (JsonObject) JsonUtil.parse(reader);

        if (JsonUtil.getFromPath(langRoot, lang) != null) {
            lang = "en";
            langRoot = (JsonObject) JsonUtil.getFromPath(langRoot, lang);
        }

        // 加载
        this.locale = langRoot;
    }


}
