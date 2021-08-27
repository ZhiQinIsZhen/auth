package com.liyz.auth.service.process.dao;

import com.liyz.auth.service.process.model.ProcessDO;
import com.liyz.auth.service.process.model.ProcessQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 14:57
 */
public interface ProcessMapper {

    List<String> selectPageProcessList(@Param("query") ProcessQuery processQuery,
                                       @Param("pageNum") Integer pageNum,
                                       @Param("pageSize") Integer pageSize);

    List<ProcessDO> selectPagedProcessDetail(@Param("list") List<String> processIdList);

    Integer selectCountProcessList(@Param("query") ProcessQuery processQuery);
}
