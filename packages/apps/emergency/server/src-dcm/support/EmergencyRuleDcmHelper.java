package com.guhecloud.cube.biz.emergency.dcm.support;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.biz.emergency.dcm.entity.DcmEmergencyRule;
import com.guhecloud.cube.biz.emergency.dcm.entity.DcmEmergencyRuleCond;
import com.guhecloud.cube.biz.emergency.dcm.entity.DcmEmergencyRuleCondExp;
import com.guhecloud.cube.biz.emergency.dcm.entity.DcmEmergencyRuleTime;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyType;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionExpService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionGroupService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleTimeService;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionExpPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionGroupPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRulePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleTimePO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductEventDTO;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.emergency.dcm.support.EmergencyDcmSupport.CONFIG_EM_RULE;
import static com.guhecloud.cube.biz.emergency.dcm.support.EmergencyDcmSupport.KW_KEY_APP_CODE;

public class EmergencyRuleDcmHelper extends AbstractGroupedDcmHelper<EmergencyRulePO, DcmEmergencyRule> {

    private final EmergencyRuleService emergencyRuleService;

    private final EmergencyRuleConditionGroupService emergencyRuleConditionGroupService;

    private final EmergencyRuleConditionService emergencyRuleConditionService;

    private final EmergencyRuleConditionExpService emergencyRuleConditionExpService;

    private final EmergencyRuleTimeService emergencyRuleTimeService;

    private final Map<String, Long> levelIdByCode = new HashMap<>();

    private final Map<Long, String> levelCodeById = new HashMap<>();

    private final Map<String, EmergencyType> typeByTypeCode = new HashMap<>();

    private final Map<String, DeviceProductInfoDTO> productByNames = new HashMap<>();

    private final Map<Long, DeviceProductInfoDTO> productById = new HashMap<>();

    private final Map<Long, ProductDTO> iotProductById = new HashMap<>();

    public EmergencyRuleDcmHelper(List<SatAlarmLevelDTO> satAlarmLevels,
                                  List<EmergencyType> emergencyTypes,
                                  List<DeviceProductInfoDTO> productDTOS,
                                  List<ProductDTO> iotProducts,
                                  EmergencyRuleService emergencyRuleService,
                                  EmergencyRuleConditionGroupService emergencyRuleConditionGroupService,
                                  EmergencyRuleConditionService emergencyRuleConditionService,
                                  EmergencyRuleConditionExpService emergencyRuleConditionExpService,
                                  EmergencyRuleTimeService emergencyRuleTimeService
                                 ) {
        super(EmergencyRulePO.class, DcmEmergencyRule.class);
        for (SatAlarmLevelDTO satAlarmLevelDTO : satAlarmLevels) {
            levelIdByCode.put(satAlarmLevelDTO.getCode(), satAlarmLevelDTO.getId());
            levelCodeById.put(satAlarmLevelDTO.getId(), satAlarmLevelDTO.getCode());
        }
        for (EmergencyType emergencyType : emergencyTypes) {
            typeByTypeCode.put(emergencyType.getCode(), emergencyType);
        }
        for (DeviceProductInfoDTO product : productDTOS) {
            productByNames.put(String.format("%s:%s:%s", product.getName(),
                                             product.getBrandName() == null ? "" : product.getBrandName(),
                                             product.getModelName() == null ? "" : product.getModelName()), product);
            productById.put(product.getId(), product);
        }
        for (ProductDTO productDTO : iotProducts) {
            iotProductById.put(Long.parseLong(productDTO.getProdId()), productDTO);
        }
        this.emergencyRuleService = emergencyRuleService;
        this.emergencyRuleConditionGroupService = emergencyRuleConditionGroupService;
        this.emergencyRuleConditionService = emergencyRuleConditionService;
        this.emergencyRuleConditionExpService = emergencyRuleConditionExpService;
        this.emergencyRuleTimeService = emergencyRuleTimeService;
    }

    public static IDcmRciConverter<DcmEmergencyRule> createDcmRciConverter() {
        return new EmergencyRuleDcmHelper(
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                null,
                null,
                null,
                null, null);
    }

    @Override
    public String getBizKey(EmergencyRulePO bizEntity) {
        return String.format("%s:%s", bizEntity.getApplicationCode(), bizEntity.getName());
    }

    @Override
    public void updateBizWithDcm(EmergencyRulePO input, DcmEmergencyRule dcmEntity) {
        Long levelId = levelIdByCode.get(dcmEntity.getAlarmLevelCode());
        EmergencyType emergencyType = typeByTypeCode.get(dcmEntity.getApplicationCode());
        input.setAlarmLevelId(levelId);
        input.setEmergencyEventTypeCode(dcmEntity.getEmergencyEventTypeCode());
        input.setEmergencyTypeId(emergencyType.getId());
        input.setEnabled(dcmEntity.getEnabled());
        input.setRepeatTime(dcmEntity.getRepeatTime());
        input.setRepeatTimeUnit(dcmEntity.getRepeatTimeUnit());
        input.setEstimatedHandlerTime(dcmEntity.getEstimatedHandlerTime());
        input.setEstimatedHandlerUnit(dcmEntity.getEstimatedHandlerUnit());
        input.setApplicationName(emergencyType.getApplicationName());
        input.setRemark(dcmEntity.getRemark() == null ? "" : dcmEntity.getRemark());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_EM_RULE;
    }

    @Override
    protected Object generateGroupKey(DcmEmergencyRule dcmEntity) {
        return String.format("%s", dcmEntity.getApplicationCode());
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmEmergencyRule> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        String appCode = (String) groupKey;
        rci.setKwargs(KW_KEY_APP_CODE, appCode);
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmEmergencyRule> dcmEntities, RequestedConfigItem rci) {
        String appCode = rci.getKwargs(KW_KEY_APP_CODE, null);
        dcmEntities.forEach(drm -> drm.setApplicationCode(appCode));
    }

    @Override
    protected void updateBizGroupInfoWithDcm(EmergencyRulePO bizEntity, DcmEmergencyRule dcmEntity) {
        Long alarmLevelId = levelIdByCode.get(dcmEntity.getAlarmLevelCode());
        EmergencyType emergencyType = typeByTypeCode.get(dcmEntity.getApplicationCode());
        bizEntity.setAlarmLevelId(alarmLevelId);
        bizEntity.setEmergencyTypeId(emergencyType.getId());
        bizEntity.setApplicationCode(emergencyType.getApplicationCode());
        bizEntity.setApplicationName(emergencyType.getApplicationName());
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmEmergencyRule dcmEntity, EmergencyRulePO bizEntity) {
        String levelCode = levelCodeById.get(bizEntity.getAlarmLevelId());
        dcmEntity.setAlarmLevelCode(levelCode);
        dcmEntity.setApplicationCode(bizEntity.getApplicationCode());
        //看需不需要查询出子表信息
    }

    public int batchAdd(Collection<EmergencyRulePO> emergencyRules, Collection<DcmEmergencyRule> dcmEmergencyRules) {
        log.debug("batch add {} products/{} dcmEntities.", emergencyRules.size(), dcmEmergencyRules.size());
        emergencyRuleService.saveBatch(emergencyRules, 100);
        // 批量新增
        batchAddAffiliateRecordsInner(emergencyRules, dcmEmergencyRules);
        return emergencyRules.size();
    }

    /**
     * 前置条件: biz entities 都是有id的
     */
    public int batchUpdate(Collection<EmergencyRulePO> bizEntities, Collection<DcmEmergencyRule> dcmEntities) {
        // 批量保存:简单删除, 再全量保存
        List<Long> recordIds = bizEntities.stream().map(EmergencyRulePO::getId).collect(Collectors.toList());
        removeAffiliateRecords(recordIds);
        batchAddAffiliateRecordsInner(bizEntities, dcmEntities);
        emergencyRuleService.updateBatchById(bizEntities, 100);
        return bizEntities.size();
    }

    public int batchRemove(Collection<EmergencyRulePO> bizEntities, Collection<DcmEmergencyRule> dcmEntities) {
        log.debug("batch remove {} EmergencyRulePO/{} dcmEntities.", bizEntities.size(), dcmEntities.size());
        List<Long> ruleIds = bizEntities.stream().map(EmergencyRulePO::getId).collect(Collectors.toList());
        removeAffiliateRecords(ruleIds);
        emergencyRuleService.removeByIds(ruleIds);
        log.debug("batch removed {} records", bizEntities.size());
        return ruleIds.size();
    }

    private void removeAffiliateRecords(Collection<Long> recordIds) {
        log.debug("remove affiliate records for {} EmergencyRulePO: ids: {}", recordIds.size(), recordIds);
        if (!recordIds.isEmpty()) {
            emergencyRuleConditionGroupService.removeByRuleIds(recordIds);
            emergencyRuleConditionService.removeByRuleIds(recordIds);
            emergencyRuleConditionExpService.removeByRuleIds(recordIds);
            emergencyRuleTimeService.removeByRuleIds(recordIds);
        }
        log.debug("remove {} EmergencyRulePO's affiliate records:  success");
    }

    private void batchAddAffiliateRecordsInner(Collection<EmergencyRulePO> emergencyRules, Collection<DcmEmergencyRule> dcmEmergencyRules) {
        List<EmergencyRuleConditionGroupPO> emergencyRuleConditionGroups = new ArrayList<>();
        List<EmergencyRuleConditionPO> emergencyRuleConditions = new ArrayList<>();
        List<EmergencyRuleConditionExpPO> emergencyRuleConditionExps = new ArrayList<>();
        List<EmergencyRuleTimePO> emergencyRuleTimes = new ArrayList<>();
        generateAffiliateRecords(emergencyRules, dcmEmergencyRules, emergencyRuleConditionGroups, emergencyRuleConditions, emergencyRuleConditionExps, emergencyRuleTimes);
        emergencyRuleConditionGroupService.saveBatch(emergencyRuleConditionGroups, 100);
        emergencyRuleConditionService.saveBatch(emergencyRuleConditions, 100);
        emergencyRuleTimeService.saveBatch(emergencyRuleTimes, 100);
        if (!CollectionUtils.isEmpty(emergencyRuleConditionExps)) {
            emergencyRuleConditionExpService.saveBatch(emergencyRuleConditionExps, 100);
        }
    }

    private void generateAffiliateRecords(Collection<EmergencyRulePO> emergencyRules,
                                          Collection<DcmEmergencyRule> dcmEmergencyRules,
                                          List<EmergencyRuleConditionGroupPO> emergencyRuleConditionGroups,
                                          List<EmergencyRuleConditionPO> emergencyRuleConditions,
                                          List<EmergencyRuleConditionExpPO> emergencyRuleConditionExps,
                                          List<EmergencyRuleTimePO> emergencyRuleTimes
                                         ) {
        for (DcmEmergencyRule dcmEmergencyRule : dcmEmergencyRules) {
            Optional<EmergencyRulePO> optionalEmergencyRulePO = emergencyRules.stream()
                    .filter(emergencyRulePO -> dcmEmergencyRule.getName().equals(emergencyRulePO.getName()) &&
                                               dcmEmergencyRule.getApplicationCode().equals(dcmEmergencyRule.getApplicationCode()))
                    .findFirst();
            if (!optionalEmergencyRulePO.isPresent()) {
                continue;
            }
            dcmEmergencyRule.getEmergencyRuleCondGroups().stream().forEach(emergencyRuleConditionGroup -> {
                EmergencyRuleConditionGroupPO emergencyRuleConditionGroupPO = EmergencyRuleConditionGroupPO.builder()
                        .id(IdWorker.getId())
                        .emergencyRuleId(optionalEmergencyRulePO.get().getId())
                        .build();
                emergencyRuleConditionGroups.add(emergencyRuleConditionGroupPO);
                emergencyRuleConditionGroup.getEmergencyRuleCondList().stream().forEach(emergencyRuleCondition -> {
                    EmergencyRuleConditionPO emergencyRuleConditionPO = convertDcm2Biz(emergencyRuleConditionGroupPO, emergencyRuleCondition);
                    emergencyRuleConditions.add(emergencyRuleConditionPO);
                    emergencyRuleConditionExps.addAll(emergencyRuleCondition.getEmergencyRuleConditionExps()
                                                              .stream()
                                                              .map(emergencyRuleConditionExp -> convertDcm2Biz(emergencyRuleConditionPO, emergencyRuleConditionExp))
                                                              .collect(Collectors.toList()));
                });
                emergencyRuleTimes.add(convertDcm2Biz(emergencyRuleConditionGroupPO, emergencyRuleConditionGroup.getEmergencyRuleTime()));
            });
        }
    }

    private EmergencyRuleConditionPO convertDcm2Biz(EmergencyRuleConditionGroupPO emergencyRuleConditionGroupPO,
                                                    DcmEmergencyRuleCond dcmEmergencyRuleCond
                                                   ) {
        DeviceProductInfoDTO deviceProductInfoDTO = productByNames.get(String.format("%s:%s:%s", dcmEmergencyRuleCond.getDeviceProdName(), dcmEmergencyRuleCond.getBrandName(), dcmEmergencyRuleCond.getModelName()));
        EmergencyRuleConditionPO emergencyRuleConditionPO = EmergencyRuleConditionPO.builder()
                .id(IdWorker.getId())
                .emergencyRuleId(emergencyRuleConditionGroupPO.getEmergencyRuleId())
                .groupId(emergencyRuleConditionGroupPO.getId())
                .deviceProdCategoryCode(deviceProductInfoDTO.getProductCategory().getCode())
                .deviceProdCategoryCodeConcat(deviceProductInfoDTO.getProductCategory().getPathCode())
                .deviceProdCategoryName(deviceProductInfoDTO.getProductCategory().getName())
                .deviceProdId(deviceProductInfoDTO.getId())
                .deviceProdName(dcmEmergencyRuleCond.getDeviceProdName())
                .conditionType(dcmEmergencyRuleCond.getConditionType())
                .deviceEventCode(dcmEmergencyRuleCond.getDeviceEventCode())
                .logicalOp(dcmEmergencyRuleCond.getLogicalOp())
                .build();
        if (EmergencyConditionTypeEnum.DEVICE_EVENT_CONDITION == dcmEmergencyRuleCond.getConditionType()) {
            ProductDTO iotProductInfo = iotProductById.get(deviceProductInfoDTO.getId());
            Optional<ProductEventDTO> eventDTO = iotProductInfo.getProductEventVoList().stream()
                    .filter(productEventDTO -> emergencyRuleConditionPO.getDeviceEventCode().equals(productEventDTO.getEventCode()))
                    .findFirst();
            eventDTO.ifPresent(event -> {
                emergencyRuleConditionPO.setDeviceEventName(event.getEventName());
                emergencyRuleConditionPO.setDeviceEventType(event.getEventConfig().getTriggerType().name());
            });
        }
        return emergencyRuleConditionPO;
    }

    private EmergencyRuleConditionExpPO convertDcm2Biz(EmergencyRuleConditionPO emergencyRuleConditionPO,
                                                       DcmEmergencyRuleCondExp dcmEmergencyRuleCondExp
                                                      ) {
        return EmergencyRuleConditionExpPO.builder()
                .emergencyRuleId(emergencyRuleConditionPO.getEmergencyRuleId())
                .groupId(emergencyRuleConditionPO.getGroupId())
                .emergencyRuleConditionId(emergencyRuleConditionPO.getId())
                .propCode(dcmEmergencyRuleCondExp.getPropCode())
                .propName(dcmEmergencyRuleCondExp.getPropName())
                .op(dcmEmergencyRuleCondExp.getOp())
                .oprand(dcmEmergencyRuleCondExp.getOprand())
                .delayTime(dcmEmergencyRuleCondExp.getDelayTime())
                .build();
    }

    private EmergencyRuleTimePO convertDcm2Biz(EmergencyRuleConditionGroupPO emergencyRuleConditionGroupPO,
                                               DcmEmergencyRuleTime emergencyRuleTime
                                              ) {
        return EmergencyRuleTimePO.builder()
                .emergencyRuleId(emergencyRuleConditionGroupPO.getEmergencyRuleId())
                .groupId(emergencyRuleConditionGroupPO.getId())
                .timeType(emergencyRuleTime.getTimeType())
                .timeConditions(emergencyRuleTime.getTimeConditions())
                .build();
    }

}
