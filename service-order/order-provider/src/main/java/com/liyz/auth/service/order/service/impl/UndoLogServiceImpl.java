package com.liyz.auth.service.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyz.auth.service.order.dao.UndoLogMapper;
import com.liyz.auth.service.order.model.UndoLogDO;
import com.liyz.auth.service.order.service.IUndoLogService;
import org.springframework.stereotype.Service;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/2 17:49
 */
@Service
public class UndoLogServiceImpl extends ServiceImpl<UndoLogMapper, UndoLogDO> implements IUndoLogService {
}
