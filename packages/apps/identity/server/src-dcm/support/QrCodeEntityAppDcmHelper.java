package com.guhecloud.cube.srv.gen.identity.dcm.support;

import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.srv.gen.identity.dcm.entity.DcmQrCodeEntityApp;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityApplicationPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityTypePO;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPE_ENTITY_APP;
import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.KW_KEY_ENTITY_TYPE;

@SuppressWarnings("CollectionIncompatibleType")
public class QrCodeEntityAppDcmHelper extends AbstractGroupedDcmHelper<QrcodeEntityApplicationPO, DcmQrCodeEntityApp> {

    private Map<String, Long> appIdByCode;
    private Map<Long, String> appCodeById;
    private Map<String, Long> entityIdByCode;
    private Map<Long, String> entityCodeById;

    public QrCodeEntityAppDcmHelper(List<ApplicationDTO> applications, List<QrcodeEntityTypePO> qrCodeEntityTypes) {
        super(QrcodeEntityApplicationPO.class, DcmQrCodeEntityApp.class);
        appIdByCode = applications.stream().collect(Collectors.toMap(ApplicationDTO::getCode, ApplicationDTO::getId));
        appCodeById = applications.stream().collect(Collectors.toMap(ApplicationDTO::getId, ApplicationDTO::getCode));
        entityCodeById = qrCodeEntityTypes.stream().collect(Collectors.toMap(QrcodeEntityTypePO::getId, QrcodeEntityTypePO::getEntityType));
        entityIdByCode = qrCodeEntityTypes.stream().collect(Collectors.toMap(QrcodeEntityTypePO::getEntityType, QrcodeEntityTypePO::getId));
    }

    public static IDcmRciConverter<DcmQrCodeEntityApp> createDcmRciConverter() {
        return new QrCodeEntityAppDcmHelper(Collections.emptyList(), Collections.emptyList());
    }

    @Override
    protected Object generateGroupKey(DcmQrCodeEntityApp dcmEntity) {
        return dcmEntity.getEntityType();
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmQrCodeEntityApp> dcmEntities) {
        RequestedConfigItem ret = getRciTemplate();
        ret.setKwargs(KW_KEY_ENTITY_TYPE, (String) groupKey);
        ret.setVargs(dcmEntities.stream().collect(Collectors.toList()));
        return ret;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmQrCodeEntityApp> dcmEntities, RequestedConfigItem rci) {
        String entityTypeCode = rci.getKwargs(KW_KEY_ENTITY_TYPE, null);
        Objects.requireNonNull(entityTypeCode, "entityType code is null");
        dcmEntities.forEach(dcm -> dcm.setEntityType(entityTypeCode));
    }

    @Override
    protected void updateBizGroupInfoWithDcm(QrcodeEntityApplicationPO bizEntity, DcmQrCodeEntityApp dcmEntity) {
        Long entityTypeId = entityIdByCode.get(dcmEntity.getEntityType());
        Objects.requireNonNull(entityTypeId, "no qrCodeEntityType entity found for entityType code " + dcmEntity.getEntityType());
        Long applicationId = appIdByCode.get(dcmEntity.getApplicationCode());
        Objects.requireNonNull(applicationId, "no application entity found for application code " + dcmEntity.getApplicationCode());
        bizEntity.setEntityId(entityTypeId);
        bizEntity.setApplicationId(applicationId);
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmQrCodeEntityApp dcmEntity, QrcodeEntityApplicationPO bizEntity) {
        String entityTypeCode = entityCodeById.get(bizEntity.getEntityId());
        Objects.requireNonNull(entityTypeCode, "invalid entityType id " + bizEntity.getEntityId());
        String applicationCode = appCodeById.get(bizEntity.getApplicationId());
        Objects.requireNonNull(applicationCode, "invalid application id " + bizEntity.getApplicationId());
        dcmEntity.setEntityType(entityTypeCode);
        dcmEntity.setApplicationCode(applicationCode);
    }

    @Override
    public String getBizKey(QrcodeEntityApplicationPO bizEntity) {
        Objects.requireNonNull(bizEntity.getApplicationId(), "applicationId cannot be null for qrCodeEntityApp");
        String appCode = appCodeById.get(bizEntity.getApplicationId());
        Objects.requireNonNull(appCode, "no applicationCode found for id " + bizEntity.getApplicationId());

        Objects.requireNonNull(bizEntity.getEntityId(), "entityTypeId cannot be null for qrCodeEntityApp");
        String entityTypeCode = entityCodeById.get(bizEntity.getEntityId());
        Objects.requireNonNull(entityTypeCode, "no entityTypeCode found for id " + bizEntity.getApplicationId());
        return String.format("%s:%s", appCode, entityTypeCode);
    }

    @Override
    public void updateBizWithDcm(QrcodeEntityApplicationPO input, DcmQrCodeEntityApp dcmEntity) {
        input.setSelected(dcmEntity.getSelected() == null ? Boolean.FALSE : dcmEntity.getSelected());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_ENTITY_APP;
    }

    @Override
    public DcmQrCodeEntityApp convertBiz2Dcm(QrcodeEntityApplicationPO qrCodeEntityApplication) {
        String entityType = entityCodeById.get(qrCodeEntityApplication.getEntityId());
        String applicationCode = appCodeById.get(qrCodeEntityApplication.getApplicationId());
        Objects.requireNonNull(entityType, "entityTypeId invalid");
        Objects.requireNonNull(applicationCode, "applicationId invalid");
        return new DcmQrCodeEntityApp(entityType, applicationCode, qrCodeEntityApplication.getSelected());
    }
}
