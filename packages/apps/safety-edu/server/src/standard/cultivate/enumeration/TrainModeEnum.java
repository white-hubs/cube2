package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration;

import lombok.Getter;

/**
 * 培训状态
 *
 * @author : appet
 * @date : 2023-4-20
 */
@Getter
public enum TrainModeEnum {
    ONLINE("线上"),
    OFFLINE("线下");

    private String desc;

    TrainModeEnum(String desc) {
        this.desc = desc;
    }
}
