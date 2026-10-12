package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 二维码实体类型创建事件
 */
@Getter
public class QrcodeEntityTypeCreatedEvent extends ApplicationEvent {

    /**
     * 类型code
     */
    private final String code;

    /**
     * 类型名称
     */
    private final String name;

    public QrcodeEntityTypeCreatedEvent(Object source, String code, String name) {
        super(source);
        this.code = code;
        this.name = name;
    }
}
