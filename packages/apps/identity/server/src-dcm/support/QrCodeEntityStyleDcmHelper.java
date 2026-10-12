package com.guhecloud.cube.srv.gen.identity.dcm.support;

import com.guhecloud.cube.srv.gen.identity.dcm.entity.DcmQrCodeEntityStyle;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityStylePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityTypePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeStylePO;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPE_ENTITY_STYLE;

@SuppressWarnings({ "UnnecessaryCheckNotNull", "ImpossibleNullComparison" })
public class QrCodeEntityStyleDcmHelper extends AbstractSimpleDcmHelper<QrcodeEntityStylePO, DcmQrCodeEntityStyle> {

    private final Map<String, Long> styleIdByName = new HashMap<>();
    private final Map<Long, String> styleNameById = new HashMap<>();

    private final Map<String, Long> entityIdByCode = new HashMap<>();
    private final Map<Long, String> entityCodeById = new HashMap<>();

    public QrCodeEntityStyleDcmHelper(List<QrcodeStylePO> qrCodeStyles, List<QrcodeEntityTypePO> qrCodeEntityTypes) {
        super(QrcodeEntityStylePO.class, DcmQrCodeEntityStyle.class);
        for (QrcodeStylePO qrcodeStylePO : qrCodeStyles) {
            styleIdByName.put(qrcodeStylePO.getName(), qrcodeStylePO.getId());
            styleNameById.put(qrcodeStylePO.getId(), qrcodeStylePO.getName());
        }
        for (QrcodeEntityTypePO qrCodeEntityType : qrCodeEntityTypes) {
            entityIdByCode.put(qrCodeEntityType.getEntityType().toString(), qrCodeEntityType.getId());
            entityCodeById.put(qrCodeEntityType.getId(), qrCodeEntityType.getEntityType().toString());
        }
    }

    public static IDcmRciConverter<DcmQrCodeEntityStyle> createDcmRciConverter() {
        return new QrCodeEntityStyleDcmHelper(Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public String getBizKey(QrcodeEntityStylePO qrCodeEntityStyle) {
        Objects.requireNonNull(qrCodeEntityStyle.getEntityType(), "entityType cannot be null for qrCodeEntityStyle");
        String entityTypeCode = entityCodeById.get(Long.valueOf(qrCodeEntityStyle.getEntityType()));
        Objects.requireNonNull(entityTypeCode, "no entityType code found for id " + qrCodeEntityStyle.getEntityType());
        return String.format("%s", entityTypeCode);
    }

    @Override
    public void updateBizWithDcm(QrcodeEntityStylePO qrCodeEntityStyle, DcmQrCodeEntityStyle dcmQrCodeEntityStyle) {
        Objects.requireNonNull(!MapUtils.isEmpty(styleIdByName), "styles cannot be null for qrCodeEntityStyle");
        Objects.requireNonNull(styleIdByName.containsKey(dcmQrCodeEntityStyle.getStyleName()), "style cannot be null for qrCodeEntityStyle");
        qrCodeEntityStyle.setStyleId(styleIdByName.get(dcmQrCodeEntityStyle.getStyleName()));
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_ENTITY_STYLE;
    }

    @Override
    public DcmQrCodeEntityStyle convertBiz2Dcm(QrcodeEntityStylePO qrCodeEntityStyle) {
        String entityType = entityCodeById.get(qrCodeEntityStyle.getEntityType());
        String styleName = styleNameById.get(qrCodeEntityStyle.getStyleId());
        Objects.requireNonNull(entityType, "entityTypeId invalid");
        Objects.requireNonNull(styleName, "styleId invalid");
        return new DcmQrCodeEntityStyle(entityType, styleName);
    }

    @Override
    public QrcodeEntityStylePO convertDcm2Biz(DcmQrCodeEntityStyle dcmEntity) {
        Long entityType = entityIdByCode.get(dcmEntity.getEntityType());
        Long styleId = styleIdByName.get(dcmEntity.getStyleName());
        Objects.requireNonNull(entityType, "entityTypeCode invalid");
        Objects.requireNonNull(styleId, "styleName invalid");
        QrcodeEntityStylePO qrcodeEntityStylePO = new QrcodeEntityStylePO();
        qrcodeEntityStylePO.setStyleId(styleId);
        qrcodeEntityStylePO.setEntityType(entityType);
        return qrcodeEntityStylePO;
    }
}
