package com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/9 11:33
 * @Description:
 */
@Getter
public enum InformationTypeEnum {

    ANNOUNCEMENT("ANNOUNCEMENT", "通知公告"),
    NEW_INFORMATION("NEW_INFORMATION", "新闻资讯");
    private final String code;
    private final String name;

    InformationTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
