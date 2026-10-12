package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 培训类型
 * @date 2024/4/1 16:18
 */
@Getter
public enum TrainTypeEnum {
    INSIDE("内部培训"),
    COMMERCIAL("商户培训");
    private final String desc;

    TrainTypeEnum(String desc) {
        this.desc = desc;
    }
}
