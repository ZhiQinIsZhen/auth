package com.liyz.auth.common.tcp.core.protocol;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:53
 */
public interface RMInboundHandler {

    /**
     * Handle branch commit response.
     *
     * @param request the request
     * @return the branch commit response
     */
    BranchCommitResponse handle(BranchCommitRequest request);

    /**
     * Handle branch rollback response.
     *
     * @param request the request
     * @return the branch rollback response
     */
    BranchRollbackResponse handle(BranchRollbackRequest request);

    /**
     * Handle delete undo log .
     *
     * @param request the request
     */
    void handle(UndoLogDeleteRequest request);
}
