package com.guhecloud.cube.base.videogw.server.common.domain.enumeration;

import lombok.Getter;

@Getter
public enum VideoFileStatusEnum {

    /**
     * 本地待存储
     */
    LOCAL_STAY_STORAGE,

    /**
     * 本地存储中
     */
    LOCAL_PROCESS_STORAGE,

    /**
     * 本地延迟存储
     */
    LOCAL_DELAY_STORAGE,

    /**
     * 本地存储成功
     */
    LOCAL_SUCCESS_STORAGE,

    /**
     * 本地存储失败
     */
    LOCAL_FAILED_STORAGE,
}
