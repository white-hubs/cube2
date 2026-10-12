package com.guhecloud.cube.biz.ouaa.server.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthTypeEnum {
    PASSWORD,
    SMS,
    WECHAT,
}
