package net;

/**
 * Created by Magina on 16/9/8.
 */
public class Config {
    public static final String LOG = "network";

    private static final int DEF_TIME_OUT = 10 * 1000;
    private static final int DEF_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    private static final int DEF_MAX_IMUM_POOL_SIZE = DEF_CORE_POOL_SIZE * 2 + 1;
    private static final long DEF_KEEP_ALIVE_TIME = 30 * 1000;
    public static final String DEF_ENCODING = "UTF-8";
    public static int CORE_POOL_SIZE = DEF_CORE_POOL_SIZE;
    public static int MAX_IMUM_POOL_SIZE = DEF_MAX_IMUM_POOL_SIZE;
    public static long KEEP_ALIVE_TIME = DEF_KEEP_ALIVE_TIME;
    public static int TIME_OUT = DEF_TIME_OUT;
    public static String ENCODING = DEF_ENCODING;
}
