package com.guhecloud.cube.base.flow.context.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 流程表单分组树形统计
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormFlowGroupCountTreeVO {

    /**
     * 分组ID
     */
    private Long formGroupId;

    /**
     * 分组名称
     */
    private String formGroupName;

    /**
     * 分组下工单数量
     */
    private Integer formGroupCount;

    /**
     * 分组下工单
     */
    private List<FormFieldVO> formGroupFlowList;

    /**
     * 上级分组ID
     */
    private Long parentId;

    /**
     * 分组层级
     */
    private Integer level;

    /**
     * 子分组
     */
    private List<FormFlowGroupCountTreeVO> children;
}
