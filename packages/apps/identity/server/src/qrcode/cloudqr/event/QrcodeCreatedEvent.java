package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 二维码创建事件
 */
@Getter
public class QrcodeCreatedEvent extends ApplicationEvent {

    /**
     * 二维码code
     */
    private final String code;

    /**
     * 二维码名称
     */
    private final String name;

    /**
     * 二维码类型code
     */
    private final String qrTypeCode;

    /**
     * 路由URL
     */
    private final String routeUrl;

    public QrcodeCreatedEvent(Object source, String code, String name, String qrTypeCode, String routeUrl) {
        super(source);
        this.code = code;
        this.name = name;
        this.qrTypeCode = qrTypeCode;
        this.routeUrl = routeUrl;
    }
}
