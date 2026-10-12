package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 二维码删除事件
 */
@Getter
public class QrcodeDeletedEvent extends ApplicationEvent {

    /**
     * 二维码code
     */
    private final String code;

    public QrcodeDeletedEvent(Object source, String code) {
        super(source);
        this.code = code;
    }
}
