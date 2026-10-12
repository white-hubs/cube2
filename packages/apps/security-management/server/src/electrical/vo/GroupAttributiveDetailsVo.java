package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo;

import lombok.Data;

@Data
public class GroupAttributiveDetailsVo {
    /**
     * 属性code
     */
    private String code;
    /**
     * 应用
     */
    private String applicationCode;
    /**
     * 组id
     */
    private Long groupId;
    private String attributiveName;
}
