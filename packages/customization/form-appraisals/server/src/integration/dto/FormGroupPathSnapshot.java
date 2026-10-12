package com.guhecloud.cube.srv.gen.appraisals.server.integration.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 流程表单分组路径快照
 */
@Data
@Builder
public class FormGroupPathSnapshot {
    /**
     * 当前分组 ID
     */
    private Long formGroupId;
    /**
     * 当前分组名称
     */
    private String formGroupName;
    /**
     * 是否末级分组
     */
    private Boolean leaf;
    /**
     * 一级分组 ID
     */
    private Long firstLevelFormGroupId;
    /**
     * 一级分组名称
     */
    private String firstLevelFormGroupName;
    /**
     * 二级分组 ID
     */
    private Long secondLevelFormGroupId;
    /**
     * 二级分组名称
     */
    private String secondLevelFormGroupName;
}
