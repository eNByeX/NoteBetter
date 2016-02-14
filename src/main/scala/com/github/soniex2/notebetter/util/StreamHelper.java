package com.github.soniex2.notebetter.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author soniex2
 */
public class StreamHelper {
    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[4096];
        int l;
        while ((l = is.read(bytes)) != -1) {
            os.write(bytes, 0, l);
        }
    }
}
