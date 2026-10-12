package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import lombok.Getter;

@Getter
public enum MessageConditionTempleEnum {
    PROPERTY_MAINTENANCE("您有一条来源于【资产管理】的消息，{content}，请及时处理"),
    PROPERTY_USAGE("您有一条来源于【资产管理】的消息，{content}，请及时处理"),
    STOCK("您有一条来源于【资产管理】的消息，{content}，请及时处理"),

    ;
    /**
     * 所属模块描述
     */
    private final String remarks;

    MessageConditionTempleEnum(String remarks) {
        this.remarks = remarks;
    }
}
