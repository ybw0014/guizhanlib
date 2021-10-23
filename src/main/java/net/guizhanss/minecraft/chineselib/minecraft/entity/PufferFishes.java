package net.guizhanss.minecraft.chineselib.minecraft.entity;

import javax.annotation.Nonnull;

public class PufferFishes {
    public static @Nonnull String getPuffState(int level){
        switch(level){
            case 0:
                return "未膨胀";
            case 1:
                return "半膨胀";
            case 2:
                return "完全膨胀";
            default:
                return "未知";
        }
    }
}
