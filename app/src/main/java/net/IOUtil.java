package net;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Magina on 16/9/9.
 */
public class IOUtil {

    private IOUtil() throws Exception {
        throw new Exception("can not init");
    }

    public static void close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.w("IOUtil", "io is null");
        }

    }

}
