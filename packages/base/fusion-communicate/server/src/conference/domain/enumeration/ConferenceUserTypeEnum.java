package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration;

import lombok.Getter;

/**
 * 会议用户类型
 *
 * @author lanchun
 * @date 2025/2/11 17:56
 */
@Getter
public enum ConferenceUserTypeEnum {

    /**
     * 房主
     */
    HOMEOWNER,

    /**
     * 管理员
     */
    ADMINISTRATORS,

    /**
     * 普通成员
     */
    ORDINARY_MEMBERS;
}
