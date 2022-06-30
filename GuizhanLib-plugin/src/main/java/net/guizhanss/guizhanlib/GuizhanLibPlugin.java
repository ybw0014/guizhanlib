package net.guizhanss.guizhanlib;

import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;

public class GuizhanLibPlugin extends JavaPlugin {
    private static GuizhanLibPlugin instance;

    public GuizhanLibPlugin() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Detect Minecraft version
        PaperLib.getMinecraftVersion();
    }

    public static GuizhanLibPlugin getInstance() {
        return instance;
    }

    public static MinecraftVersion getMinecraftVersion() {
        getInstance()
    }
}
