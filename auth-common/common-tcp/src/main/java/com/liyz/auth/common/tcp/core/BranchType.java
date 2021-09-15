package com.liyz.auth.common.tcp.core;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:17
 */
public enum BranchType {

    /**
     * The At.
     */
    // AT Branch
    AT,

    /**
     * The TCC.
     */
    TCC,

    /**
     * The SAGA.
     */
    SAGA,

    /**
     * The XA.
     */
    XA;

    /**
     * Get branch type.
     *
     * @param ordinal the ordinal
     * @return the branch type
     */
    public static BranchType get(byte ordinal) {
        return get((int)ordinal);
    }

    /**
     * Get branch type.
     *
     * @param ordinal the ordinal
     * @return the branch type
     */
    public static BranchType get(int ordinal) {
        for (BranchType branchType : values()) {
            if (branchType.ordinal() == ordinal) {
                return branchType;
            }
        }
        throw new IllegalArgumentException("Unknown BranchType[" + ordinal + "]");
    }

    /**
     * Get branch type.
     *
     * @param name the name
     * @return the branch type
     */
    public static BranchType get(String name) {
        for (BranchType branchType : values()) {
            if (branchType.name().equalsIgnoreCase(name)) {
                return branchType;
            }
        }
        throw new IllegalArgumentException("Unknown BranchType[" + name + "]");
    }
}
