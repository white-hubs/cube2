package com.guhecloud.cube.srv.gen.work.permit.server.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 作业等级。
 */
@Getter
@AllArgsConstructor
public enum WorkPermitLevelEnum {
    /**
     * 一级作业。
     */
    LEVEL_ONE("LEVEL_ONE", "一级"),
    /**
     * 二级作业。
     */
    LEVEL_TWO("LEVEL_TWO", "二级"),
    /**
     * 三级作业。
     */
    LEVEL_THREE("LEVEL_THREE", "三级"),
    /**
     * 四级作业。
     */
    LEVEL_FOUR("LEVEL_FOUR", "四级");

    private final String code;
    private final String name;

    public static List<WorkPermitLevelEnum> list() {
        return Arrays.asList(values());
    }
}
