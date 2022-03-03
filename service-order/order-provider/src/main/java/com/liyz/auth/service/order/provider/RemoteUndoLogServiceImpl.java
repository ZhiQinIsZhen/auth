package com.liyz.auth.service.order.provider;

import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.service.order.bo.UndoLogBO;
import com.liyz.auth.service.order.remote.RemoteUndoLogService;
import com.liyz.auth.service.order.service.IUndoLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/2 17:52
 */
@Slf4j
@DubboService
public class RemoteUndoLogServiceImpl implements RemoteUndoLogService {

    @Resource
    private IUndoLogService iUndoLogService;

    @Override
    public List<UndoLogBO> list() {
        return CommonCloneUtil.ListClone(iUndoLogService.list(), UndoLogBO.class);
    }
}
