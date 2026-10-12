package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo;

import lombok.Data;

@Data
public class SupervisorOptionVO {
    /**
     * ouaa 用户主键。
     */
    private Long userId;

    /**
     * 用户姓名。
     */
    private String name;

    /**
     * 脱敏后的手机号，用于下拉辅助展示。
     */
    private String mobileMasked;

    /**
     * 组织机构名称，ouaa 有返回时填充。
     */
    private String organizationName;
}
