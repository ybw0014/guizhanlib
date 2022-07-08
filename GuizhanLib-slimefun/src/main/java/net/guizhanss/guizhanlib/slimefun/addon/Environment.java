package net.guizhanss.guizhanlib.slimefun.addon;

/**
 * This class represents runtime environments
 *
 * @author ybw0014
 */
public enum Environment {
    /**
     * This is used when the Addon is running in a real server
     */
    LIVE,

    /**
     * This is used when the Addon is running in testing environment
     */
    TESTING,

    /**
     * This is used when the library is under testing
     */
    LIB_TESTING
}
