package net.guizhanss.guizhanlib.minecraft.commands;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class stores the usage of a command.
 * Can also validate the args based on the usage.
 *
 * @author ybw0014
 */
@SuppressWarnings("ConstantConditions")
public final class Usage {
    private static final Pattern USAGE_ARG_PATTERN = Pattern.compile("<[^>]*>|\\[[^]]*\\]|\\S+");
    private final String usageStr;

    public Usage(@Nonnull String usage) {
        Preconditions.checkArgument(usage != null, "usage cannot be null");
        this.usageStr = usage.trim();
    }

    @Nonnull
    public String get() {
        return usageStr;
    }

    public boolean isValid(@Nonnull String[] args) {
        if (args.length < getRequiredArgsCount()) {
            return false;
        } else {
            return args.length <= parseUsage().length;
        }
    }

    private String[] parseUsage() {
        List<String> args = new ArrayList<>();
        Matcher matcher = USAGE_ARG_PATTERN.matcher(usageStr);

        while (matcher.find()) {
            args.add(matcher.group());
        }

        return args.toArray(new String[0]);
    }

    private int getRequiredArgsCount() {
        int count = 0;
        for (String arg : parseUsage()) {
            if (arg.startsWith("<")) {
                count++;
            }
        }
        return count;
    }
}
