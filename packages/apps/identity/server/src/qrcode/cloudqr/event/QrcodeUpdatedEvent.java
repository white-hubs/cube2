package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 二维码更新事件
 */
@Getter
public class QrcodeUpdatedEvent extends ApplicationEvent {

    /**
     * 二维码code
     */
    private final String code;

    /**
     * 二维码名称
     */
    private final String name;

    /**
     * 路由URL
     */
    private final String routeUrl;

    public QrcodeUpdatedEvent(Object source, String code, String name, String routeUrl) {
        super(source);
        this.code = code;
        this.name = name;
        this.routeUrl = routeUrl;
    }
}
