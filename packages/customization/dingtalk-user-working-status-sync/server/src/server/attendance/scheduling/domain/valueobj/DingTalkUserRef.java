package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.valueobj;

import lombok.Data;

@Data
public class DingTalkUserRef {

    /**
     * 钉钉用户id
     */
    private String dingTalkUserId;

    /**
     * 系统用户id
     */
    private Long systemUserId;
}
