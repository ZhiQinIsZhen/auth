package com.liyz.auth.service.process.provider;

import com.liyz.auth.service.process.bo.ProcessDeployBO;
import com.liyz.auth.service.process.remote.RemoteProcessDeployService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 14:29
 */
@Slf4j
@DubboService
public class RemoteProcessDeployServiceImpl implements RemoteProcessDeployService {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 上传流程图
     *
     * @param processDeployBO
     * @return
     */
    @Override
    public Boolean deploy(ProcessDeployBO processDeployBO) {
        Boolean result = Boolean.FALSE;
        InputStream inputStream = new ByteArrayInputStream(processDeployBO.getBytes());
        if (processDeployBO.getFileName().endsWith(".bpmn")) {
            repositoryService.createDeployment().addInputStream(processDeployBO.getFileName(), inputStream).deploy();
            result = Boolean.TRUE;
        } else if (processDeployBO.getFileName().endsWith(".zip")) {
            repositoryService.createDeployment().addZipInputStream(new ZipInputStream(inputStream)).deploy();
            result = Boolean.TRUE;
        }
        return result;
    }
}
