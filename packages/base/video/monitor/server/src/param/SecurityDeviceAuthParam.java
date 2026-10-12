package com.guhecloud.cube.biz.vmonitor.server.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

import java.util.List;

@Data
public class SecurityDeviceAuthParam {
    /**
     * 用户信息
     */
    @Size(min = 1, message = "用户不能为空")
    List<AuthUserParam> userList;
    /**
     * 设备id
     */
    private List<Long> securityDeviceId;

    private Boolean deviceAll;
    private String applicationCode;
    private List<String> areaId;
    private String keyword;
    private Boolean associated;
    private Long userId;

}
