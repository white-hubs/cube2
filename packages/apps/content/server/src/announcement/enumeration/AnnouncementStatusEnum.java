package com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/8 15:07
 * @Description:
 */
@Getter
public enum AnnouncementStatusEnum {
    HIDE("HIDE", "已隐藏"),
    DISPLAY("DISPLAY", "已显示");
    private final String code;
    private final String name;

    AnnouncementStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
