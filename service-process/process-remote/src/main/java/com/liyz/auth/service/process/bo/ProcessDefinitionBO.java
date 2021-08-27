package com.liyz.auth.service.process.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 16:08
 */
@Getter
@Setter
public class ProcessDefinitionBO implements Serializable {
    private static final long serialVersionUID = -281105883862896490L;

    private String id;

    private String category;

    private String name;

    private String key;

    private String description;

    private int version;

    private String resourceName;

    private String deploymentId;

    String diagramResourceName;

    private boolean startFormKey;

    boolean isGraphicalNotationDefined;

    boolean isSuspended;

    private String tenantId;
}
