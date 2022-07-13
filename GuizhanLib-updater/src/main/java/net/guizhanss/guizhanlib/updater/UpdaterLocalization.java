package net.guizhanss.guizhanlib.updater;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.guizhanss.guizhanlib.utils.JsonUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("ConstantConditions")
class UpdaterLocalization {
    private final JsonObject localizations;

    @ParametersAreNonnullByDefault
    public UpdaterLocalization(InputStream stream) {
        Preconditions.checkArgument(stream != null, "Input stream cannot be null");

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        localizations = (JsonObject) JsonUtil.parse(reader);
    }

    @ParametersAreNonnullByDefault
    @Nullable
    public String getLocalization(String lang, String path) {
        JsonElement local = JsonUtil.getFromPath(localizations, lang + "." + path);
        if (local.isJsonObject()) {
            return local.getAsString();
        } else {
            return null;
        }
    }
}
