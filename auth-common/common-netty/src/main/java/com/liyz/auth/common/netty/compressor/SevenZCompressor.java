package com.liyz.auth.common.netty.compressor;

import com.liyz.auth.common.netty.loader.LoadLevel;
import com.liyz.auth.common.netty.util.SevenZUtil;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:45
 */
@LoadLevel(name = "SEVENZ")
public class SevenZCompressor implements Compressor {

    @Override
    public byte[] compress(byte[] bytes) {
        return SevenZUtil.compress(bytes);
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return SevenZUtil.decompress(bytes);
    }

}
