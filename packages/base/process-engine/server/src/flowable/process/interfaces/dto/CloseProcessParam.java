package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import lombok.Data;

/**
 * @author pengjian
 * @date 2022/9/1 22:14
 * @description
 */
@Data
public class CloseProcessParam {
    private String processInstanceId;
    private String businessKey;
    private String comment;
}
