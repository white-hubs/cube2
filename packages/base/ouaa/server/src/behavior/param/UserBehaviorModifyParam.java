package com.guhecloud.cube.biz.ouaa.server.behavior.param;

import lombok.Data;

@Data
public class UserBehaviorModifyParam {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 终端编号
     */
    private String terminal;

    /**
     * 用户行为配置
     */
    private String behaviorConfig;

}
