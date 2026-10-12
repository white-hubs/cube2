package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration;

import lombok.Getter;

/**
 * 培训状态
 *
 * @author : appet
 * @date : 2023-4-20
 */
@Getter
public enum TrainTargetEnum {
    STAFF("员工"),
    MERCHANT("商户");

    private String desc;

    TrainTargetEnum(String desc) {
        this.desc = desc;
    }
}
