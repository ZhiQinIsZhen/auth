package com.liyz.auth.common.netty.thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/9 15:40
 */
public class PositiveAtomicCounter {

    private static final int MASK = 0x7FFFFFFF;
    private final AtomicInteger atom;

    public PositiveAtomicCounter() {
        atom = new AtomicInteger(0);
    }

    public final int incrementAndGet() {
        return atom.incrementAndGet() & MASK;
    }

    public final int getAndIncrement() {
        return atom.getAndIncrement() & MASK;
    }

    public int get() {
        return atom.get() & MASK;
    }
}
