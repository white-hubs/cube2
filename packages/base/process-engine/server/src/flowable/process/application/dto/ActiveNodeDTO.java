package com.guhecloud.cube.base.flow.flowable.process.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/10/25 18:08
 * @description 活跃节点信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveNodeDTO {
    /**
     * 节点名称
     */
    private String name;

    private String taskDefinitionKey;

    private String processInstanceId;
}
