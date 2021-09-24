package com.liyz.auth.common.netty.compressor;

import com.liyz.auth.common.netty.loader.LoadLevel;
import com.liyz.auth.common.netty.util.BZip2Util;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:32
 */
@LoadLevel(name = "BZIP2")
public class BZip2Compressor implements Compressor{

    @Override
    public byte[] compress(byte[] bytes) {
        return BZip2Util.compress(bytes);
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return BZip2Util.decompress(bytes);
    }
}
