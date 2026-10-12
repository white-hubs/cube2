package com.guhecloud.cube.biz.vmonitor.server.param.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 部门
 *
 * @author lanchun
 * @date 2023/3/22 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DepartmentAuthParam {

    /**
     * 部门主键
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;
}
