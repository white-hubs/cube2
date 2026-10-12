package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.Data;

/**
 * @author lzy
 * @date 2021/11/9
 */
@Data
public class FlowDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程类型主键
     */
    private Long flowTypeId;

    /**
     * 可用状态
     */
    private Boolean enabled;

    /**
     * 流程键
     */
    private String processKey;
}
