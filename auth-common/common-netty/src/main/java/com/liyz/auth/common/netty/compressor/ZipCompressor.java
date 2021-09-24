package com.liyz.auth.common.netty.compressor;

import com.liyz.auth.common.netty.loader.LoadLevel;
import com.liyz.auth.common.netty.util.ZipUtil;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:49
 */
@LoadLevel(name = "ZIP")
public class ZipCompressor implements Compressor {

    @Override
    public byte[] compress(byte[] bytes) {
        return ZipUtil.compress(bytes);
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return ZipUtil.decompress(bytes);
    }

}
