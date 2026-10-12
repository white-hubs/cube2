package com.guhecloud.cube.srv.gen.identity.dcm.support;

import com.guhecloud.cube.srv.gen.identity.dcm.entity.DcmQrCodeApp;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeApplicationPO;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPE_APP;

public class QrCodeAppDcmHelper extends AbstractSimpleDcmHelper<QrcodeApplicationPO, DcmQrCodeApp> {

    public QrCodeAppDcmHelper() {
        super(QrcodeApplicationPO.class, DcmQrCodeApp.class);
    }

    public static IDcmRciConverter<DcmQrCodeApp> createDcmRciConverter() {
        return new QrCodeAppDcmHelper();
    }

    @Override
    public String getBizKey(QrcodeApplicationPO qrCodeApplication) {
        return String.format("%s", qrCodeApplication.getApplicationCode());
    }

    @Override
    public void updateBizWithDcm(QrcodeApplicationPO qrCodeApplication, DcmQrCodeApp dcmQrCodeApp) {
        qrCodeApplication.setApplicationCode(dcmQrCodeApp.getCode());
        qrCodeApplication.setApplicationName(dcmQrCodeApp.getName());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_APP;
    }

    @Override
    public DcmQrCodeApp convertBiz2Dcm(QrcodeApplicationPO bizEntity) {
        return DcmQrCodeApp.builder()
                .code(bizEntity.getApplicationCode())
                .name(bizEntity.getApplicationName())
                .build();
    }

    @Override
    public QrcodeApplicationPO convertDcm2Biz(DcmQrCodeApp dcmEntity) {
        QrcodeApplicationPO qrcodeApplicationPO = new QrcodeApplicationPO();
        qrcodeApplicationPO.setApplicationCode(dcmEntity.getCode());
        qrcodeApplicationPO.setApplicationName(dcmEntity.getName());
        return qrcodeApplicationPO;
    }
}
