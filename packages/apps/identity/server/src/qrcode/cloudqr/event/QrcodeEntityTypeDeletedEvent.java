package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 二维码实体类型删除事件
 */
@Getter
public class QrcodeEntityTypeDeletedEvent extends ApplicationEvent {

    /**
     * 类型code
     */
    private final String code;

    public QrcodeEntityTypeDeletedEvent(Object source, String code) {
        super(source);
        this.code = code;
    }
}
