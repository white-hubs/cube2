package com.guhecloud.cube.srv.gen.visitor.management.server.flow.enumeration;

import com.guhecloud.cube.srv.gen.visitor.management.server.flow.po.VisitorInvitePO;
import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/13 15:31
 * @Description: 访客流程枚举
 */
@Getter
public enum VisitorFlowEnum {

    VISITOR_INVITE("访客邀约", VisitorInvitePO.class);
    private final String desc;

    private final Class clazz;

    VisitorFlowEnum(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }
}
