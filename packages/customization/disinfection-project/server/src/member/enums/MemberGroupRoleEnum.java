package com.guhecloud.cube.srv.gen.disinfection.server.member.enums;

import lombok.Getter;

@Getter
public enum MemberGroupRoleEnum {
    // 管理
    MANAGER("管理者"),
    // 成员
    MEMBER("普通成员"),
    ;
    final String displayName;

    MemberGroupRoleEnum(String displayName) {
        this.displayName = displayName;
    }
}
