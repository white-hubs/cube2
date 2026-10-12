package com.guhecloud.cube.srv.gen.visitor.management.server.enums;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/24 17:46
 * @Description: 访客邀约状态枚举
 */
@Getter
public enum VisitorInviteStausEnum {

    PENDING("待处理"),
    PROCESSING("处理中"),
    REJECTED("已驳回"),
    COMPLETE("已完成"),
    EXPIRED("已失效"),
    ;

    final String desc;

    VisitorInviteStausEnum(String desc) {
        this.desc = desc;
    }
}
