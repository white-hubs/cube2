package com.guhecloud.cube.gm.meattrade.plugin.tally.domain.enumeration;

import lombok.Getter;

@Getter
public enum TallyPushStatusEnum {

    /**
     * 未推送
     */
    NOT_PUSHED,

    /**
     * 已推送
     */
    PUSHED,

    /**
     * 推送失败
     */
    PUSH_FAILED;
}
