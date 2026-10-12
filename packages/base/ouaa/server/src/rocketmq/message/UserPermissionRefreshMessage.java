package com.guhecloud.cube.biz.ouaa.server.rocketmq.message;

import lombok.Data;

import java.util.Set;

@Data
public class UserPermissionRefreshMessage {

    public static final String TOPIC = "USER_PERMISSION_REFRESH";

    /**
     * 用户主键集合
     */
    private Set<Long> userId;
}
