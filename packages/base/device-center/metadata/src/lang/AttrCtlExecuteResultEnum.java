package com.guhecloud.cube.base.device.center.metadata.lang;

/**
 * @author pengjian
 * @date 2022/7/12 16:17
 * @description 属性控制结果
 */
public enum AttrCtlExecuteResultEnum {

    /**
     * 命令送达给物联网平台
     */
    ARRIVAL,
    /**
     * 下发中
     */
    ISSUING,
    /**
     * 成功
     */
    SUCCESS,
    /**
     * 失败
     */
    FAIL,
    /**
     * 超时
     */
    EXPIRED
}
