package com.guhecloud.platform.event.src.enumeration;

import lombok.Getter;

@Getter
public enum InnerEventEnum {

    /**
     * 平台内部事件
     */
    PLATFORM_INNER("platform_inner", PublisherTypeEnum.ASYNC);

    private final String name;

    private final PublisherTypeEnum publisherType;

    InnerEventEnum(String name, PublisherTypeEnum publisherType) {
        this.name = name;
        this.publisherType = publisherType;
    }
}
