package com.guhecloud.cube.base.flow.context.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 16:44
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormFlowGroupCountVO {

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
}
