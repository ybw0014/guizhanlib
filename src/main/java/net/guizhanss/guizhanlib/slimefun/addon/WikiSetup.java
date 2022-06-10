package net.guizhanss.guizhanlib.slimefun.addon;

import com.google.common.base.Preconditions;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

/**
 * 设置附属的Wiki.
 *
 * 需要最新汉化粘液开发版, 否则功能不会生效.
 */
@UtilityClass
public final class WikiSetup {
    /**
     * 调用方法加载wiki.json
     *
     * @param plugin 附属 {@link SlimefunAddon} 实例
     */
    public static void setupJson(@Nonnull Plugin plugin) {
        Preconditions.checkNotNull(plugin, "Plugin instance should not be null");
        Preconditions.checkArgument(plugin instanceof SlimefunAddon, "Plugin must implements SlimefunAddon");

        try {
            Class<?> clazz = Class.forName("net.guizhanss.slimefun4.utils.WikiUtils");
            clazz.getMethod("setupJson", Plugin.class).invoke(null, plugin);
        } catch (ClassNotFoundException | NoSuchMethodException | NullPointerException
            | IllegalAccessException | InvocationTargetException e) {
            plugin.getLogger().log(Level.WARNING, "无法进行Wiki设置，请确保使用的粘液科技版本为最新开发版");
        }
    }

    /**
     * 调用方法添加物品的wiki页面
     *
     * @param item 粘液物品{@link SlimefunItem}实例
     * @param wikiPage Wiki页面
     */
    @ParametersAreNonnullByDefault
    public static void setupItem(SlimefunItem item, String wikiPage) {
        Preconditions.checkNotNull(item, "SlimefunItem should not be null");
        Preconditions.checkNotNull(wikiPage, "WikiPage should not be null");

        try {
            Class<?> clazz = item.getClass();
            clazz.getMethod("addWikiPage", String.class).invoke(item, wikiPage);
        } catch (NoSuchMethodException | NullPointerException
            | IllegalAccessException | InvocationTargetException ignored) {
        }
    }
}
