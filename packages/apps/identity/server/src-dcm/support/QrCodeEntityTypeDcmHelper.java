package com.guhecloud.cube.srv.gen.identity.dcm.support;

import com.guhecloud.cube.srv.gen.identity.dcm.entity.DcmQrCodeEntityType;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityTypePO;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPE_ENTITY_TYPE;

public class QrCodeEntityTypeDcmHelper extends AbstractSimpleDcmHelper<QrcodeEntityTypePO, DcmQrCodeEntityType> {

    public QrCodeEntityTypeDcmHelper() {
        super(QrcodeEntityTypePO.class, DcmQrCodeEntityType.class);
    }

    public static IDcmRciConverter<DcmQrCodeEntityType> createDcmRciConverter() {
        return new QrCodeEntityTypeDcmHelper();
    }

    @Override
    public String getBizKey(QrcodeEntityTypePO qrCodeEntityType) {
        return String.format("%s", qrCodeEntityType.getEntityType());
    }

    @Override
    public void updateBizWithDcm(QrcodeEntityTypePO qrCodeEntityType, DcmQrCodeEntityType dcmQrCodeEntityType) {
        qrCodeEntityType.setTypeName(dcmQrCodeEntityType.getName() == null ? "" : dcmQrCodeEntityType.getName());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_ENTITY_TYPE;
    }

    @Override
    public QrcodeEntityTypePO convertDcm2Biz(DcmQrCodeEntityType dcmEntity) {
        QrcodeEntityTypePO qrcodeEntityTypePO = new QrcodeEntityTypePO();
        qrcodeEntityTypePO.setTypeName(dcmEntity.getName());
        qrcodeEntityTypePO.setEntityType(dcmEntity.getEntityType());
        return qrcodeEntityTypePO;
    }

    @Override
    public DcmQrCodeEntityType convertBiz2Dcm(QrcodeEntityTypePO bizEntity) {
        DcmQrCodeEntityType dcmQrCodeEntityType = new DcmQrCodeEntityType();
        dcmQrCodeEntityType.setName(bizEntity.getTypeName());
        dcmQrCodeEntityType.setEntityType(bizEntity.getEntityType());
        return dcmQrCodeEntityType;
    }
}
