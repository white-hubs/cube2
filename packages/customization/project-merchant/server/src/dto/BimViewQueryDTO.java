package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BimViewQueryDTO {

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 视觉名称
     */
    private String name;

    /**
     * 楼层名称
     */
    private String modelLevel;

    /**
     * 专业名称
     */
    private String specialty;

    /**
     * 构建编码
     */
    private String ecode;

    /**
     * 视觉配置
     */
    private String viewconfig;

}
