package com.liyz.auth.common.netty.compressor;

import com.liyz.auth.common.netty.loader.LoadLevel;
import com.liyz.auth.common.netty.util.Lz4Util;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:42
 */
@LoadLevel(name = "LZ4")
public class Lz4Compressor implements Compressor {
    @Override
    public byte[] compress(byte[] bytes) {
        return Lz4Util.compress(bytes);
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return Lz4Util.decompress(bytes);
    }
}
