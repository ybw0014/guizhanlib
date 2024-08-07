package net.guizhanss.guizhanlib.utils;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

@UtilityClass
public final class FileUtil {
    @Nonnull
    @ParametersAreNonnullByDefault
    public List<String> listJarEntries(File jarFile, BiPredicate<String, JarEntry> predicate, BiFunction<String, JarEntry, String> mapper) throws IOException {
        Preconditions.checkArgument(predicate != null, "predicate should not be null");
        Preconditions.checkArgument(mapper != null, "mapper should not be null");
        if (jarFile == null || !jarFile.isFile()) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        try (JarInputStream stream = new JarInputStream(new FileInputStream(jarFile))) {
            JarEntry entry;
            while ((entry = stream.getNextJarEntry()) != null) {
                String entryName = entry.getName();
                if (predicate.test(entryName, entry)) {
                    result.add(mapper.apply(entryName, entry));
                }
            }
        }
        return result;
    }
}
