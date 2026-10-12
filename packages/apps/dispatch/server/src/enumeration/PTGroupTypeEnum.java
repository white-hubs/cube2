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
public enum PTGroupTypeEnum {

    GT_0("固定组"),

    GT_1("临时组"),
    ;

    /**
     * 描述信息
     */
    private final String desc;

    PTGroupTypeEnum(@NonNull String desc) {
        this.desc = desc;
    }
}
