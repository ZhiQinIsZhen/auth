package com.liyz.auth.service.staff.remote;

import com.liyz.auth.service.staff.bo.UndoLogBO;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/2 17:51
 */
public interface RemoteUndoLogService {

    List<UndoLogBO> list();
}
