package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报警处理人
 *
 * @author lanchun
 * @date 2023/2/28 13:43
 */
@Getter
@AllArgsConstructor
public class AlarmHandlerUser {

    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 部门
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;
}
