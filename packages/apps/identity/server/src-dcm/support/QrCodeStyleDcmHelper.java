package com.guhecloud.cube.srv.gen.identity.dcm.support;

import com.guhecloud.cube.srv.gen.identity.dcm.entity.DcmQrCodeStyle;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeStylePO;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPE_STYLE;

public class QrCodeStyleDcmHelper extends AbstractSimpleDcmHelper<QrcodeStylePO, DcmQrCodeStyle> {

    public QrCodeStyleDcmHelper() {
        super(QrcodeStylePO.class, DcmQrCodeStyle.class);
    }

    public static IDcmRciConverter<DcmQrCodeStyle> createDcmRciConverter() {
        return new QrCodeStyleDcmHelper();
    }

    @Override
    public String getBizKey(QrcodeStylePO qrCodeStyle) {
        return String.format("%s:%s", qrCodeStyle.getCode(), qrCodeStyle.getName());
    }

    @Override
    public void updateBizWithDcm(QrcodeStylePO qrCodeStyle, DcmQrCodeStyle dcmQrCodeStyle) {
        qrCodeStyle.setWidth(dcmQrCodeStyle.getWidth() == null ? "0" : dcmQrCodeStyle.getWidth().toString());
        qrCodeStyle.setHeight(dcmQrCodeStyle.getHeight() == null ? "0" : dcmQrCodeStyle.getHeight().toString());
        qrCodeStyle.setConfigJson(dcmQrCodeStyle.getConfigJson() == null ? "" : dcmQrCodeStyle.getConfigJson());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_STYLE;
    }
}
