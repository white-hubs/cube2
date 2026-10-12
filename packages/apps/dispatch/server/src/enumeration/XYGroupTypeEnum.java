package com.guhecloud.cube.biz.dispatch.server.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 普天组类型枚举
 *
 * @author appet
 * @date 2022/8/1
 */
@Getter
public enum XYGroupTypeEnum {

    GT_0("对讲组"),

    GT_1("机构组"),
    ;

    /**
     * 描述信息
     */
    private final String desc;

    XYGroupTypeEnum(@NonNull String desc) {
        this.desc = desc;
    }
}
