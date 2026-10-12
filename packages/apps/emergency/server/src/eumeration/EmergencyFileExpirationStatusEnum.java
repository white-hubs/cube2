package com.guhecloud.cube.biz.emergency.server.eumeration;

import lombok.Getter;

/**
 * 预案文件到期状态枚举
 *
 * @author lanchun
 * @date 2024/5/16 16:15
 */
@Getter
public enum EmergencyFileExpirationStatusEnum {

    /**
     * 正常的
     */
    NORMAL,

    /**
     * 过期的
     */
    EXPIRED
}
