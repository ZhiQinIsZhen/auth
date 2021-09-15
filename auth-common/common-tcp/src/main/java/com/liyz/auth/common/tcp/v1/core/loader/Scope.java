package com.liyz.auth.common.tcp.v1.core.loader;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/9 16:05
 */
public enum Scope {

    /**
     * The extension will be loaded in singleton mode
     */
    SINGLETON,

    /**
     * The extension will be loaded in multi instance mode
     */
    PROTOTYPE
}
