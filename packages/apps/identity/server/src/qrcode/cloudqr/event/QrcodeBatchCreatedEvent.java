package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event;

import com.guhecloud.cube.base.cloud.qr.code.sdk.model.QrCodeDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 二维码批量创建事件
 */
@Getter
public class QrcodeBatchCreatedEvent extends ApplicationEvent implements Supplier<List<QrCodeDTO>>, Consumer<List<QrCodeDTO>> {

    /**
     * 批量创建项列表
     */
    private final List<QrcodeCreateItem> items;

    private List<QrCodeDTO> qrCodeDTOS;

    public QrcodeBatchCreatedEvent(Object source, List<QrcodeCreateItem> items) {
        super(source);
        this.items = items;
    }

    @Override
    public void accept(List<QrCodeDTO> qrCodeDTOS) {
        this.qrCodeDTOS = qrCodeDTOS;
    }

    @Override
    public List<QrCodeDTO> get() {
        return this.qrCodeDTOS;
    }

    /**
     * 二维码创建项
     */
    @Getter
    public static class QrcodeCreateItem {
        private final String code;
        private final String name;
        private final String qrTypeCode;
        private final String routeUrl;

        public QrcodeCreateItem(String code, String name, String qrTypeCode, String routeUrl) {
            this.code = code;
            this.name = name;
            this.qrTypeCode = qrTypeCode;
            this.routeUrl = routeUrl;
        }
    }
}
