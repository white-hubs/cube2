package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 二维码实体类型更新事件
 */
@Getter
public class QrcodeEntityTypeUpdatedEvent extends ApplicationEvent {

    /**
     * 类型ID
     */
    private final Long id;

    /**
     * 类型code
     */
    private final String code;

    /**
     * 类型名称
     */
    private final String name;

    public QrcodeEntityTypeUpdatedEvent(Object source, Long id, String code, String name) {
        super(source);
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
