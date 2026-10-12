package com.guhecloud.cube.biz.ouaa.server.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WechatResultEnum {
    /**
     * 等待，成功，失败，拒绝
     */
    PENDING,
    SUCCESS,
    FAIL,
    DENY;
}
