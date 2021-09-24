package com.liyz.auth.common.netty.util;

import lombok.experimental.UtilityClass;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.bzip2.CBZip2OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:34
 */
@UtilityClass
public class BZip2Util {

    private static final int BUFFER_SIZE = 8192;

    public static byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (CBZip2OutputStream bzip2 = new CBZip2OutputStream(bos)) {
            bzip2.write(bytes);
            bzip2.finish();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("BZip2 compress error", e);
        }
    }

    public static byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try (CBZip2InputStream bzip2 = new CBZip2InputStream(bis)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = bzip2.read(buffer)) > -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("BZip2 decompress error", e);
        }
    }
}
