package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration;

import lombok.Getter;

/**
 * 培训状态
 *
 * @author : appet
 * @date : 2023-4-20
 */
@Getter
public enum TrainSignEnum {
    SIGNED("已签"),
    UNSIGNED("未签");

    private String desc;

    TrainSignEnum(String desc) {
        this.desc = desc;
    }
}
