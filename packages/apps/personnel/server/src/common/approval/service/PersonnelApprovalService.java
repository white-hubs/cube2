package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.service;

import com.guhecloud.cube.srv.gen.personnel.management.server.common.enumeration.PersonnelApprovalBizEnum;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/22 13:50
 * @Description: 人事管理审批流程接口定义
 */
public interface PersonnelApprovalService<T> {

    /**
     * 获取支持的枚举类型
     */
    PersonnelApprovalBizEnum getOperationType();

    /**
     * 前置业务处理
     */
    void preBusinessHandle(T entity);

    /**
     * 审批通过后处理
     */
    void postBusinessHandle(T entity);
}
