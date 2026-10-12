package com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 17:25
 * @Description: 证件类型
 */
@Getter
public enum PaperType {

    ID_CARD("身份证");

    private final String desc;

    PaperType(String desc) {
        this.desc = desc;
    }
}
