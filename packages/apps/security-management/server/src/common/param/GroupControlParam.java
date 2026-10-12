package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import lombok.Data;

import java.util.List;

@Data
public class GroupControlParam {
    /**
     * 群控组名称
     */
    private String groupControlName;
    /**
     * 密码
     */
    private String password;
    /**
     * 组归属
     */
    private String ascription;
    /**
     * 回路id
     */
    private List<GroupControlDetailParam> deviceDetails;
}
