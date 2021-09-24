package com.liyz.auth.common.netty.serializer;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 18:09
 */
public interface PbConvertor<T, S> {

    public S convert2Proto(T t);

    public T convert2Model(S s);
}
