package com.guhecloud.cube.biz.emergency.server.rule.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.apps.linkage.metadata.enumeration.DateUnitEnum;
import com.guhecloud.cube.base.alarm.client.SatAlarmLevelClient;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyTypeMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyEventType;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyType;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleDisableService;
import com.guhecloud.cube.biz.emergency.server.rule.application.querygateway.query.EmergencyRuleApplicationQuery;
import com.guhecloud.cube.biz.emergency.server.rule.application.querygateway.query.EmergencyRuleCollectionQuery;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionDevicePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleDisablePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRulePO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleConditionDeviceMapper;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleMapper;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleCollectionVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleConditionDeviceCollectionVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleDetailVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleSafetyReportVO;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyEventTypeService;
import com.guhecloud.cube.biz.linkage.client.LinkageClient;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 预案规则查询服务
 *
 * @author lanchun
 * @date 2022/9/5 14:21
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleQueryService {

    private final EmergencyRuleMapper emergencyRuleMapper;

    private final EmergencyRuleGroupQueryService emergencyRuleGroupQueryService;

    private final EmergencyRuleConditionDeviceMapper emergencyRuleConditionDeviceMapper;

    private final EmergencyTypeMapper emergencyTypeMapper;

    private final EmergencyEventTypeService emergencyEventTypeService;

    private final EmergencyRuleDisableService emergencyRuleDisableService;

    private final SatAlarmLevelClient satAlarmLevelClient;

    private final LinkageClient linkageClient;

    /**
     * 预案规则详情
     *
     * @param emergencyRuleId
     *
     * @return
     */
    public EmergencyRuleDetailVO getDetail(Long emergencyRuleId) {
        var emergencyRulePO = this.emergencyRuleMapper.selectById(emergencyRuleId);
        if (Objects.isNull(emergencyRulePO)) {
            return null;
        }
        //查询关联的规则条件
        var emergencyRuleConditionGroupVOS = emergencyRuleGroupQueryService.getRuleGroups(emergencyRuleId);
        //查询关联的设备列表
        var emergencyRuleConditionDevicePOS = this.emergencyRuleConditionDeviceMapper.selectList(Wrappers
                                                                                                         .<EmergencyRuleConditionDevicePO>lambdaQuery()
                                                                                                         .eq(EmergencyRuleConditionDevicePO::getEmergencyRuleId, emergencyRuleId));
        var emergencyRuleDetailVO = ObjectUtils.copying(emergencyRulePO, EmergencyRuleDetailVO.class);
        emergencyRuleDetailVO.setEmergencyRuleConditionGroups(emergencyRuleConditionGroupVOS);
        emergencyRuleDetailVO.setEmergencyRuleConditionDevices(ObjectUtils.copying(emergencyRuleConditionDevicePOS, EmergencyRuleConditionDeviceCollectionVO.class));
        emergencyRuleDetailVO.setAppointDevice(
                Objects.isNull(emergencyRulePO.getAppointDevice()) ? !CollectionUtils.isEmpty(emergencyRuleConditionDevicePOS)
                                                                   : emergencyRulePO.getAppointDevice());
        var satAlarmLevelDTO = this.satAlarmLevelClient.getAlarmLevel(emergencyRuleDetailVO.getAlarmLevelId());
        if (Objects.nonNull(satAlarmLevelDTO)) {
            emergencyRuleDetailVO.setAlarmLevelName(satAlarmLevelDTO.getName());
        }
        var emergencyType = this.emergencyTypeMapper.selectById(emergencyRuleDetailVO.getEmergencyTypeId());
        if (Objects.nonNull(emergencyType)) {
            emergencyRuleDetailVO.setEmergencyTypeName(emergencyType.getName());
        }
        if (Objects.nonNull(emergencyRulePO.getRepeatTime())) {
            emergencyRuleDetailVO.setRepeatTimeName(
                    emergencyRulePO.getRepeatTime() + DateUnitEnum.valueOf(emergencyRulePO.getRepeatTimeUnit()).getDescription());
        }
        if (Objects.nonNull(emergencyRulePO.getEstimatedHandlerTime())) {
            emergencyRuleDetailVO.setEstimatedHandlerTimeName(emergencyRulePO.getEstimatedHandlerTime() +
                                                              DateUnitEnum.valueOf(emergencyRulePO.getEstimatedHandlerUnit())
                                                                      .getDescription());
        }
        EmergencyEventType emergencyEventType = null;
        if (StringUtils.isNotEmpty(emergencyRuleDetailVO.getEmergencyEventTypeCode())) {
            emergencyEventType = this.emergencyEventTypeService.getByCode(emergencyRuleDetailVO.getEmergencyEventTypeCode());
        }
        if (Objects.nonNull(emergencyEventType)) {
            emergencyRuleDetailVO.setEmergencyEventTypeName(emergencyEventType.getName());
            emergencyRuleDetailVO.setEmergencyEventTypeId(emergencyEventType.getId());
        }
        //查询禁用配置
        var emergencyRuleDisableListByRuleIds = this.emergencyRuleDisableService.getEmergencyRuleDisableListByRuleIds(Collections.singleton(emergencyRuleDetailVO.getId()));
        if (!CollectionUtils.isEmpty(emergencyRuleDisableListByRuleIds)) {
            emergencyRuleDetailVO.setDisableDuration(emergencyRuleDisableListByRuleIds.getFirst().getDuration());
        }
        emergencyRuleDetailVO.setEmergencyActions(this.linkageClient.getLinkageActions(emergencyRuleDetailVO.getId()));
        emergencyRuleDetailVO.setEmergencyActionBeforeList(this.linkageClient.getLinkageActionBeforeList(emergencyRuleDetailVO.getId()));
        return emergencyRuleDetailVO;

    }

    /**
     * 预案规则分页列表
     *
     * @param emergencyRuleCollectionQuery
     *
     * @return
     */
    public Pagination<EmergencyRuleCollectionVO> listPage(EmergencyRuleCollectionQuery emergencyRuleCollectionQuery) {
        Page<EmergencyRulePO> emergencyRulePOPage = this.emergencyRuleMapper.selectPage(PagingUtils.convert(emergencyRuleCollectionQuery),
                                                                                        Wrappers.<EmergencyRulePO>lambdaQuery()
                                                                                                .like(StringUtils.isNotEmpty(emergencyRuleCollectionQuery.getKeyword()), EmergencyRulePO::getName, emergencyRuleCollectionQuery.getKeyword())
                                                                                                .eq(Objects.nonNull(emergencyRuleCollectionQuery.getAlarmLevelId()), EmergencyRulePO::getAlarmLevelId, emergencyRuleCollectionQuery.getAlarmLevelId())
                                                                                                .eq(Objects.nonNull(emergencyRuleCollectionQuery.getEmergencyTypeId()), EmergencyRulePO::getEmergencyTypeId, emergencyRuleCollectionQuery.getEmergencyTypeId())
                                                                                                .eq(StringUtils.isNotEmpty(emergencyRuleCollectionQuery.getApplicationCode()), EmergencyRulePO::getApplicationCode, emergencyRuleCollectionQuery.getApplicationCode())
                                                                                                .eq(StringUtils.isNotEmpty(emergencyRuleCollectionQuery.getEmergencyEventTypeCode()), EmergencyRulePO::getEmergencyEventTypeCode, emergencyRuleCollectionQuery.getEmergencyEventTypeCode())
                                                                                                .orderByDesc(EmergencyRulePO::getCreateTime)
                                                                                       );
        List<EmergencyRuleCollectionVO> emergencyRuleCollectionVOS = ObjectUtils.copying(emergencyRulePOPage.getRecords(), EmergencyRuleCollectionVO.class);
        if (CollectionUtils.isEmpty(emergencyRuleCollectionVOS)) {
            return Pagination.empty();
        }
        //预案类型主键
        Set<Long> emergencyTypeIds = emergencyRuleCollectionVOS.stream().map(EmergencyRuleCollectionVO::getEmergencyTypeId)
                .collect(Collectors.toSet());
        Map<Long, EmergencyType> emergencyTypes = emergencyTypeMapper.selectList(Wrappers.<EmergencyType>lambdaQuery()
                                                                                         .in(EmergencyType::getId, emergencyTypeIds))
                .stream()
                .collect(Collectors.toMap(EmergencyType::getId, emergencyType -> emergencyType, (o, n) -> n));
        //报警级别列表
        Set<Long> alarmLevelIds = emergencyRuleCollectionVOS.stream().map(EmergencyRuleCollectionVO::getAlarmLevelId)
                .collect(Collectors.toSet());

        Map<Long, SatAlarmLevelDTO> satAlarmLevelDTOMap = this.satAlarmLevelClient.getByIds(alarmLevelIds).stream()
                .collect(Collectors.toMap(SatAlarmLevelDTO::getId, satAlarmLevelDTO -> satAlarmLevelDTO, (o, n) -> n));
        //报警事件编号
        Set<String> eventTypeCodes = emergencyRuleCollectionVOS.stream().map(EmergencyRuleCollectionVO::getEmergencyEventTypeCode)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());
        Map<String, EmergencyEventType> emergencyEventTypeMap = this.emergencyEventTypeService.getByCodes(eventTypeCodes)
                .stream()
                .collect(Collectors.toMap(EmergencyEventType::getCode, emergencyEventType -> emergencyEventType, (o, n) -> n));
        //规则禁用配置
        Set<Long> ruleIds = emergencyRuleCollectionVOS.stream()
                .map(EmergencyRuleCollectionVO::getId)
                .collect(Collectors.toSet());
        Map<Long, EmergencyRuleDisablePO> emergencyRuleDisablePOMap = this.emergencyRuleDisableService
                .getEmergencyRuleDisableListByRuleIds(ruleIds)
                .stream()
                .collect(Collectors.toMap(EmergencyRuleDisablePO::getRuleId, emergencyRuleDisable -> emergencyRuleDisable, (o, n) -> n));
        emergencyRuleCollectionVOS.forEach(emergencyRuleCollectionVO -> {
            if (satAlarmLevelDTOMap.containsKey(emergencyRuleCollectionVO.getAlarmLevelId())) {
                emergencyRuleCollectionVO.setAlarmLevelName(satAlarmLevelDTOMap.get(emergencyRuleCollectionVO.getAlarmLevelId()).getName());
            }
            if (emergencyTypes.containsKey(emergencyRuleCollectionVO.getEmergencyTypeId())) {
                emergencyRuleCollectionVO.setEmergencyTypeName(emergencyTypes.get(emergencyRuleCollectionVO.getEmergencyTypeId())
                                                                       .getName());
            }
            if (emergencyEventTypeMap.containsKey(emergencyRuleCollectionVO.getEmergencyEventTypeCode())) {
                emergencyRuleCollectionVO.setEmergencyEventTypeName(emergencyEventTypeMap
                                                                            .get(emergencyRuleCollectionVO.getEmergencyEventTypeCode())
                                                                            .getName());
                emergencyRuleCollectionVO.setEmergencyEventTypeId(emergencyEventTypeMap
                                                                          .get(emergencyRuleCollectionVO.getEmergencyEventTypeCode())
                                                                          .getId());
            }
            if (emergencyRuleDisablePOMap.containsKey(emergencyRuleCollectionVO.getId())) {
                emergencyRuleCollectionVO.setDisableDuration(emergencyRuleCollectionVO.getDisableDuration());
            }
        });
        return PagingUtils.convert(emergencyRulePOPage, emergencyRuleCollectionVOS);
    }

    public List<EmergencyRuleDetailVO> getRuleByEventCode(@NonNull Long modelId, @NonNull String eventCode) {
        return this.emergencyRuleMapper.selectRuleByEventCode(eventCode, modelId, true);
    }

    public Integer getEventEmergencyTenant(@NonNull String eventCode, @NonNull Long deviceId) {
        return this.emergencyRuleMapper.selectEventEmergencyTenant(eventCode, deviceId);
    }

    public List<EmergencyRuleSafetyReportVO> emergencyRuleByAlarmAndApplication(EmergencyRuleApplicationQuery emergencyRuleApplicationQuery
                                                                               ) {

        return emergencyRuleMapper.selectEmergencyRuleSetInfo(emergencyRuleApplicationQuery);
    }
}
