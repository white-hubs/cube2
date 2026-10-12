package com.guhecloud.cube.base.videogw.server.common.domain.enumeration;

import lombok.Getter;

@Getter
public enum VideoCommandStatusEnum {

    /**
     * 下发中
     */
    ISSUE_PROGRESS,

    /**
     * 下发成功
     */
    ISSUE_SUCCESS,

    /**
     * 下发失败
     */
    ISSUE_FAIL;
}
