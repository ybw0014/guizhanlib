package net.guizhanss.guizhanlib.updater;

import java.math.BigInteger;
import java.util.regex.Pattern;
import java.util.logging.Logger;

final class VersionComparison {
    private static final Pattern VERSION = Pattern.compile("([0-9]+(?:\\.[0-9]+)*)(?:-([0-9A-Za-z-]+(?:\\.[0-9A-Za-z-]+)*))?(?:\\+([0-9A-Za-z-]+(?:\\.[0-9A-Za-z-]+)*))?");
    private static final Pattern NUMBER = Pattern.compile("[0-9]+");

    private VersionComparison() {}

    static boolean needsUpdate(String remote, String local, Logger logger) {
        var r = VERSION.matcher(remote);
        var l = VERSION.matcher(local);
        if (!r.matches() || !l.matches()) {
            logger.warning("Updater version comparison fallback: versions are not parseable; inequality may cause a downgrade.");
            return !remote.equals(local);
        }
        var a = r.group(1).split("\\.");
        var b = l.group(1).split("\\.");
        for (int i = 0; i < Math.max(a.length, b.length); i++) {
            int cmp = number(i < a.length ? a[i] : "0").compareTo(number(i < b.length ? b[i] : "0"));
            if (cmp != 0) return cmp > 0;
        }
        String rp = r.group(2), lp = l.group(2);
        if (rp == null || lp == null) return rp == null && lp != null;
        a = rp.split("\\.");
        b = lp.split("\\.");
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            boolean an = NUMBER.matcher(a[i]).matches(), bn = NUMBER.matcher(b[i]).matches();
            int cmp = an && bn ? number(a[i]).compareTo(number(b[i])) : an != bn ? (an ? -1 : 1) : a[i].compareTo(b[i]);
            if (cmp != 0) return cmp > 0;
        }
        return a.length > b.length;
    }

    private static BigInteger number(String value) {
        return new BigInteger(value);
    }
}
