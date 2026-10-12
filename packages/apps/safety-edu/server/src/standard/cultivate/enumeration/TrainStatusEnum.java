package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration;

import lombok.Getter;

/**
 * 培训状态
 *
 * @author : appet
 * @date : 2023-4-20
 */
@Getter
public enum TrainStatusEnum {
    WAIT_PUBLISH("待发布"),
    PUBLISHED("待培训"),
    REVOKED("已撤销"),
    TRAINING("培训中"),
    OVER("已结束");

    private String desc;

    TrainStatusEnum(String desc) {
        this.desc = desc;
    }
}
