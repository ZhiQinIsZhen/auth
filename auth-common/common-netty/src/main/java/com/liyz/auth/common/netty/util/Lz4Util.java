package com.liyz.auth.common.netty.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.jpountz.lz4.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:42
 */
@Slf4j
@UtilityClass
public class Lz4Util {

    private static final int ARRAY_SIZE = 1024;

    public static byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        LZ4Compressor compressor = LZ4Factory.fastestInstance().fastCompressor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (LZ4BlockOutputStream lz4BlockOutputStream
                     = new LZ4BlockOutputStream(outputStream, ARRAY_SIZE, compressor)) {
            lz4BlockOutputStream.write(bytes);
        } catch (IOException e) {
            log.error("compress bytes error", e);
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(ARRAY_SIZE);

        LZ4FastDecompressor decompressor = LZ4Factory.fastestInstance().fastDecompressor();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try (LZ4BlockInputStream decompressedInputStream
                     = new LZ4BlockInputStream(inputStream, decompressor)) {
            int count;
            byte[] buffer = new byte[ARRAY_SIZE];
            while ((count = decompressedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
        } catch (IOException e) {
            log.error("decompress bytes error", e);
        }
        return outputStream.toByteArray();
    }
}
