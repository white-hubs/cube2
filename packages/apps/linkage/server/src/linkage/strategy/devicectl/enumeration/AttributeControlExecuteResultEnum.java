package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.enumeration;

import lombok.Getter;

/**
 * @author pengjian
 * @date 2022/7/12 16:17
 * @description 属性控制结果
 */
@Getter
public enum AttributeControlExecuteResultEnum {

    /**
     * 命令送达给物联网
     */
    ARRIVAL("命令送达给物联网"),
    /**
     * 下发中
     */
    ISSUING("下发中"),
    /**
     * 成功
     */
    SUCCESS("成功"),
    /**
     * 失败
     */
    FAIL("失败"),
    /**
     * 超时
     */
    EXPIRED("超时");

    /**
     * 描述信息
     */
    private final String description;

    AttributeControlExecuteResultEnum(String description) {
        this.description = description;
    }
}
