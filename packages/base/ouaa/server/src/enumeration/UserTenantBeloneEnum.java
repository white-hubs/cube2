package com.guhecloud.cube.biz.ouaa.server.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTenantBeloneEnum {
    BELONG_SYSTEM,
    BELONG,
    BELONG_MINI_PROGRAM,
    NOT_BELONG;
}
