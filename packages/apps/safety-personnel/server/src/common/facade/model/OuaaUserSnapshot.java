package com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.model;

import lombok.Builder;
import lombok.Data;

/**
 * OUAA 用户展示快照。
 */
@Data
@Builder
public class OuaaUserSnapshot {

    /**
     * 用户姓名。
     */
    private String userName;

    /**
     * 手机号。
     */
    private String phone;

    /**
     * 部门名称。
     */
    private String deptName;

    /**
     * 岗位名称。
     */
    private String postName;

    /**
     * 单位/租户名称。
     */
    private String companyName;
}
