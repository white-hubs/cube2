package com.guhecloud.cube.srv.gen.safety.education.server.common.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/18 11:17
 * @Description:
 */
@Getter
public enum SafetyEducationFileSceneCodeEnum {
    SAFETY_EDUCATION_COURSEWARE("SAFETY_EDUCATION::COURSEWARE", "安全教育_课间库"),
    SAFETY_EDUCATION_CULTIVATE_PHOTO("SAFETY_EDUCATION::CULTIVATE_PHOTO", "安全教育_培训记录照片"),
    SAFETY_EDUCATION_GROUP_PUBLISH("SAFETY_EDUCATION::GROUP_PUBLISH", "安全教育_集团下发"),

    ;

    private final String code;

    private final String desc;

    SafetyEducationFileSceneCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
