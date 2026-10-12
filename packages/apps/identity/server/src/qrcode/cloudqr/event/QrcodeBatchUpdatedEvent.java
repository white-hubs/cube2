package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event;

import com.guhecloud.cube.base.cloud.qr.code.sdk.model.QrCodeDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 二维码批量更新事件
 */
@Getter
public class QrcodeBatchUpdatedEvent extends ApplicationEvent implements Supplier<List<QrCodeDTO>>, Consumer<List<QrCodeDTO>> {

    /**
     * 批量更新项列表
     */
    private final List<QrcodeUpdateItem> items;

    /**
     * 更新后的二维码信息
     */
    private List<QrCodeDTO> qrCodeDTOS;

    public QrcodeBatchUpdatedEvent(Object source, List<QrcodeUpdateItem> items) {
        super(source);
        this.items = items;
    }

    @Override
    public List<QrCodeDTO> get() {
        return this.qrCodeDTOS;
    }

    @Override
    public void accept(List<QrCodeDTO> qrCodeDTOS) {
        this.qrCodeDTOS = qrCodeDTOS;
    }

    /**
     * 二维码更新项
     */
    @Getter
    public static class QrcodeUpdateItem {
        private final String code;
        private final String name;
        private final String routeUrl;

        public QrcodeUpdateItem(String code, String name, String routeUrl) {
            this.code = code;
            this.name = name;
            this.routeUrl = routeUrl;
        }
    }
}
