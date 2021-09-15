package com.liyz.auth.common.tcp.core;

import com.liyz.auth.common.tcp.core.protocol.*;
import com.liyz.auth.common.tcp.core.rpc.RpcContext;
import com.liyz.auth.common.tcp.core.rpc.TCInboundHandler;
import com.liyz.auth.common.tcp.exception.AbstractExceptionHandler;
import com.liyz.auth.common.tcp.exception.StoreException;
import com.liyz.auth.common.tcp.exception.TransactionException;
import com.liyz.auth.common.tcp.exception.TransactionExceptionCode;
import com.liyz.auth.common.tcp.v1.core.model.GlobalStatus;
import com.liyz.auth.common.tcp.v1.session.GlobalSession;
import lombok.extern.slf4j.Slf4j;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 9:58
 */
@Slf4j
public abstract class AbstractTCInboundHandler extends AbstractExceptionHandler implements TCInboundHandler {

    @Override
    public GlobalBeginResponse handle(GlobalBeginRequest request, final RpcContext rpcContext) {
        GlobalBeginResponse response = new GlobalBeginResponse();
        exceptionHandleTemplate(new AbstractCallback<GlobalBeginRequest, GlobalBeginResponse>() {
            @Override
            public void execute(GlobalBeginRequest request, GlobalBeginResponse response) throws TransactionException {
                try {
                    doGlobalBegin(request, response, rpcContext);
                } catch (StoreException e) {
                    throw new TransactionException(TransactionExceptionCode.FailedStore,
                            String.format("begin global request failed. xid=%s, msg=%s", response.getXid(), e.getMessage()),
                            e);
                }
            }
        }, request, response);
        return response;
    }

    /**
     * Do global begin.
     *
     * @param request    the request
     * @param response   the response
     * @param rpcContext the rpc context
     * @throws TransactionException the transaction exception
     */
    protected abstract void doGlobalBegin(GlobalBeginRequest request, GlobalBeginResponse response,
                                          RpcContext rpcContext) throws TransactionException;

    @Override
    public GlobalCommitResponse handle(GlobalCommitRequest request, final RpcContext rpcContext) {
        GlobalCommitResponse response = new GlobalCommitResponse();
        response.setGlobalStatus(GlobalStatus.Committing);
        exceptionHandleTemplate(new AbstractCallback<GlobalCommitRequest, GlobalCommitResponse>() {
            @Override
            public void execute(GlobalCommitRequest request, GlobalCommitResponse response)
                    throws TransactionException {
                try {
                    doGlobalCommit(request, response, rpcContext);
                } catch (StoreException e) {
                    throw new TransactionException(TransactionExceptionCode.FailedStore,
                            String.format("global commit request failed. xid=%s, msg=%s", request.getXid(), e.getMessage()),
                            e);
                }
            }
            @Override
            public void onTransactionException(GlobalCommitRequest request, GlobalCommitResponse response,
                                               TransactionException tex) {
                super.onTransactionException(request, response, tex);
                checkTransactionStatus(request, response);
            }

            @Override
            public void onException(GlobalCommitRequest request, GlobalCommitResponse response, Exception rex) {
                super.onException(request, response, rex);
                checkTransactionStatus(request, response);
            }


        }, request, response);
        return response;
    }

    /**
     * Do global commit.
     *
     * @param request    the request
     * @param response   the response
     * @param rpcContext the rpc context
     * @throws TransactionException the transaction exception
     */
    protected abstract void doGlobalCommit(GlobalCommitRequest request, GlobalCommitResponse response,
                                           RpcContext rpcContext) throws TransactionException;

    @Override
    public GlobalRollbackResponse handle(GlobalRollbackRequest request, final RpcContext rpcContext) {
        GlobalRollbackResponse response = new GlobalRollbackResponse();
        response.setGlobalStatus(GlobalStatus.Rollbacking);
        exceptionHandleTemplate(new AbstractCallback<GlobalRollbackRequest, GlobalRollbackResponse>() {
            @Override
            public void execute(GlobalRollbackRequest request, GlobalRollbackResponse response)
                    throws TransactionException {
                try {
                    doGlobalRollback(request, response, rpcContext);
                } catch (StoreException e) {
                    throw new TransactionException(TransactionExceptionCode.FailedStore, String
                            .format("global rollback request failed. xid=%s, msg=%s", request.getXid(), e.getMessage()), e);
                }
            }

            @Override
            public void onTransactionException(GlobalRollbackRequest request, GlobalRollbackResponse response,
                                               TransactionException tex) {
                super.onTransactionException(request, response, tex);
                // may be appears StoreException outer layer method catch
                checkTransactionStatus(request, response);
            }

            @Override
            public void onException(GlobalRollbackRequest request, GlobalRollbackResponse response, Exception rex) {
                super.onException(request, response, rex);
                // may be appears StoreException outer layer method catch
                checkTransactionStatus(request, response);
            }
        }, request, response);
        return response;
    }

    /**
     * Do global rollback.
     *
     * @param request    the request
     * @param response   the response
     * @param rpcContext the rpc context
     * @throws TransactionException the transaction exception
     */
    protected abstract void doGlobalRollback(GlobalRollbackRequest request, GlobalRollbackResponse response,
                                             RpcContext rpcContext) throws TransactionException;

    @Override
    public BranchRegisterResponse handle(BranchRegisterRequest request, final RpcContext rpcContext) {
        BranchRegisterResponse response = new BranchRegisterResponse();
        exceptionHandleTemplate(new AbstractCallback<BranchRegisterRequest, BranchRegisterResponse>() {
            @Override
            public void execute(BranchRegisterRequest request, BranchRegisterResponse response)
                    throws TransactionException {
                try {
                    doBranchRegister(request, response, rpcContext);
                } catch (StoreException e) {
                    throw new TransactionException(TransactionExceptionCode.FailedStore, String
                            .format("branch register request failed. xid=%s, msg=%s", request.getXid(), e.getMessage()), e);
                }
            }
        }, request, response);
        return response;
    }

    /**
     * Do branch register.
     *
     * @param request    the request
     * @param response   the response
     * @param rpcContext the rpc context
     * @throws TransactionException the transaction exception
     */
    protected abstract void doBranchRegister(BranchRegisterRequest request, BranchRegisterResponse response,
                                             RpcContext rpcContext) throws TransactionException;

    @Override
    public BranchReportResponse handle(BranchReportRequest request, final RpcContext rpcContext) {
        BranchReportResponse response = new BranchReportResponse();
        exceptionHandleTemplate(new AbstractCallback<BranchReportRequest, BranchReportResponse>() {
            @Override
            public void execute(BranchReportRequest request, BranchReportResponse response)
                    throws TransactionException {
                try {
                    doBranchReport(request, response, rpcContext);
                } catch (StoreException e) {
                    throw new TransactionException(TransactionExceptionCode.FailedStore, String
                            .format("branch report request failed. xid=%s, branchId=%s, msg=%s", request.getXid(),
                                    request.getBranchId(), e.getMessage()), e);
                }
            }
        }, request, response);
        return response;
    }

    /**
     * Do branch report.
     *
     * @param request    the request
     * @param rpcContext the rpc context
     * @throws TransactionException the transaction exception
     */
    protected abstract void doBranchReport(BranchReportRequest request, BranchReportResponse response,
                                           RpcContext rpcContext) throws TransactionException;

    @Override
    public GlobalLockQueryResponse handle(GlobalLockQueryRequest request, final RpcContext rpcContext) {
        GlobalLockQueryResponse response = new GlobalLockQueryResponse();
        exceptionHandleTemplate(new AbstractCallback<GlobalLockQueryRequest, GlobalLockQueryResponse>() {
            @Override
            public void execute(GlobalLockQueryRequest request, GlobalLockQueryResponse response)
                    throws TransactionException {
                try {
                    doLockCheck(request, response, rpcContext);
                } catch (StoreException e) {
                    throw new TransactionException(TransactionExceptionCode.FailedStore, String
                            .format("global lock query request failed. xid=%s, msg=%s", request.getXid(), e.getMessage()),
                            e);
                }
            }
        }, request, response);
        return response;
    }

    /**
     * Do lock check.
     *
     * @param request    the request
     * @param response   the response
     * @param rpcContext the rpc context
     * @throws TransactionException the transaction exception
     */
    protected abstract void doLockCheck(GlobalLockQueryRequest request, GlobalLockQueryResponse response,
                                        RpcContext rpcContext) throws TransactionException;

    @Override
    public GlobalStatusResponse handle(GlobalStatusRequest request, final RpcContext rpcContext) {
        GlobalStatusResponse response = new GlobalStatusResponse();
        response.setGlobalStatus(GlobalStatus.UnKnown);
        exceptionHandleTemplate(new AbstractCallback<GlobalStatusRequest, GlobalStatusResponse>() {
            @Override
            public void execute(GlobalStatusRequest request, GlobalStatusResponse response)
                    throws TransactionException {
                try {
                    doGlobalStatus(request, response, rpcContext);
                } catch (StoreException e) {
                    throw new TransactionException(TransactionExceptionCode.FailedStore,
                            String.format("global status request failed. xid=%s, msg=%s", request.getXid(), e.getMessage()),
                            e);
                }
            }

            @Override
            public void onTransactionException(GlobalStatusRequest request, GlobalStatusResponse response,
                                               TransactionException tex) {
                super.onTransactionException(request, response, tex);
                checkTransactionStatus(request, response);
            }

            @Override
            public void onException(GlobalStatusRequest request, GlobalStatusResponse response, Exception rex) {
                super.onException(request, response, rex);
                checkTransactionStatus(request, response);
            }
        }, request, response);
        return response;
    }

    /**
     * Do global status.
     *
     * @param request    the request
     * @param response   the response
     * @param rpcContext the rpc context
     * @throws TransactionException the transaction exception
     */
    protected abstract void doGlobalStatus(GlobalStatusRequest request, GlobalStatusResponse response,
                                           RpcContext rpcContext) throws TransactionException;

    @Override
    public GlobalReportResponse handle(GlobalReportRequest request, final RpcContext rpcContext) {
        GlobalReportResponse response = new GlobalReportResponse();
        response.setGlobalStatus(request.getGlobalStatus());
        exceptionHandleTemplate(new AbstractCallback<GlobalReportRequest, GlobalReportResponse>() {
            @Override
            public void execute(GlobalReportRequest request, GlobalReportResponse response)
                    throws TransactionException {
                doGlobalReport(request, response, rpcContext);
            }
        }, request, response);
        return response;
    }

    /**
     * Do global report.
     *
     * @param request    the request
     * @param response   the response
     * @param rpcContext the rpc context
     * @throws TransactionException the transaction exception
     */
    protected abstract void doGlobalReport(GlobalReportRequest request, GlobalReportResponse response,
                                           RpcContext rpcContext) throws TransactionException;

    private void checkTransactionStatus(AbstractGlobalEndRequest request, AbstractGlobalEndResponse response) {
        try {
            GlobalSession globalSession = SessionHolder.findGlobalSession(request.getXid(), false);
            if (globalSession != null) {
                response.setGlobalStatus(globalSession.getStatus());
            } else {
                response.setGlobalStatus(GlobalStatus.Finished);
            }
        } catch (Exception exx) {
            log.error("check transaction status error,{}]", exx.getMessage());
        }
    }
}
