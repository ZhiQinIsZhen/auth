package com.liyz.auth.common.netty.compressor;

import com.liyz.auth.common.netty.constant.CompressorType;
import com.liyz.auth.common.netty.loader.LoadLevel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:29
 */
public class CompressorFactory {

    /**
     * The constant COMPRESSOR_MAP.
     */
    protected static final Map<CompressorType, Compressor> COMPRESSOR_MAP = new ConcurrentHashMap<>();

    static {
        COMPRESSOR_MAP.put(CompressorType.NONE, new NoneCompressor());
        COMPRESSOR_MAP.put(CompressorType.BZIP2, new BZip2Compressor());
        COMPRESSOR_MAP.put(CompressorType.DEFLATER, new DeflaterCompressor());
        COMPRESSOR_MAP.put(CompressorType.LZ4, new Lz4Compressor());
        COMPRESSOR_MAP.put(CompressorType.SEVENZ, new SevenZCompressor());
        COMPRESSOR_MAP.put(CompressorType.ZIP, new ZipCompressor());
    }

    /**
     * Get compressor by code.
     *
     * @param code the code
     * @return the compressor
     */
    public static Compressor getCompressor(byte code) {
        CompressorType type = CompressorType.getByCode(code);
        return COMPRESSOR_MAP.get(type);
    }

    /**
     * None compressor
     */
    @LoadLevel(name = "NONE")
    public static class NoneCompressor implements Compressor {
        @Override
        public byte[] compress(byte[] bytes) {
            return bytes;
        }

        @Override
        public byte[] decompress(byte[] bytes) {
            return bytes;
        }
    }
}
