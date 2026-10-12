package com.guhecloud.cube.biz.ouaa.server.enumeration;

import lombok.Getter;

@Getter
public enum OuaaCode {

    OUAA("认证中心");

    @Getter
    String label;

    OuaaCode(String label) {
        this.label = label;
    }
}
