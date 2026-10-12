package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/20 10:50
 * @Description: 人事离职配置
 */

@Data
public class PersonnelLeaveConfigVO {

    /**
     * 是否只需离职
     */
    private Boolean onlyLeaveFlag = true;
}
