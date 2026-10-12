package com.guhecloud.cube.base.flow.context.querygateway.query;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 16:47
 * @Description:
 */
@Data
public class FormFlowGroupQueryParam {

    /**
     * 应用
     */
    private String application;

    /**
     * 工单名称
     */
    private String keyword;

    /**
     * 是否过滤空集合
     */
    private Boolean filterEmpty;
    private List<Long> formFlowIds;
}
