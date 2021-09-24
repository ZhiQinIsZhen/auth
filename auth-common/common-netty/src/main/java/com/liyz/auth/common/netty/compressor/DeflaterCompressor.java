package com.liyz.auth.common.netty.compressor;

import com.liyz.auth.common.netty.loader.LoadLevel;
import com.liyz.auth.common.netty.util.DeflaterUtil;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:38
 */
@LoadLevel(name = "DEFLATER")
public class DeflaterCompressor implements Compressor {

    @Override
    public byte[] compress(byte[] bytes) {
        return DeflaterUtil.compress(bytes);
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return DeflaterUtil.decompress(bytes);
    }

}
