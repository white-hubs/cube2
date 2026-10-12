package com.guhecloud.cube.biz.vmonitor.server.auth.vo;

import lombok.Data;

import java.util.List;

@Data
public class AuthVO {

    private Long id;

    /**
     * 授权范围集合
     */
    private List<AuthScopeVO> authScopes;

    /**
     * 设备数量
     */
    private Integer deviceCount;

    /**
     * 权限归属集合
     */
    private List<AuthGrantTargetVO> grantTargets;
}
