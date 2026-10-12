package com.guhecloud.cube.base.flow.flowable.resolver.service;

import com.guhecloud.cube.base.flow.flowable.resolver.dto.ConditionInfo;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/12 11:15
 * @Description:
 */
public interface BpmnResolverService {

    /**
     * 条件表达式
     *
     * @param conditionInfo
     *
     * @return
     */
    StringBuffer conditionExpression(ConditionInfo conditionInfo);
}
