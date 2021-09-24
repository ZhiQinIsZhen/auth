package com.liyz.auth.common.netty.compressor;

import com.liyz.auth.common.netty.loader.LoadLevel;
import com.liyz.auth.common.netty.util.GzipUtil;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:41
 */
@LoadLevel(name = "GZIP")
public class GzipCompressor implements Compressor {

    @Override
    public byte[] compress(byte[] bytes) {
        return GzipUtil.compress(bytes);
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return GzipUtil.decompress(bytes);
    }

}
