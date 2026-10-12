package com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/18 15:16
 * @Description:
 */

@Getter
public enum DisplayStatusEnum {
    OPEN("开启"),
    CLOSE("关闭");
    private final String desc;

    DisplayStatusEnum(String desc) {
        this.desc = desc;
    }
}
