package com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/16 17:48
 * @Description:
 */

@Getter
public enum ContentFileSceneCode {
    CONTENT_ANNOUNCEMENT("CONTENT::ANNOUNCEMENT", "通知公告"),
    CONTENT_BUILDING_DATA("CONTENT::BUILDING_DATA", "建筑资料"),
    CONTENT_DEVICE_DOCUMENT("CONTENT::DEVICE_DOCUMENT", "设备文档"),
    ;

    private final String code;
    private final String desc;

    ContentFileSceneCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
