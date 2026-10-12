package com.guhecloud.cube.iot.mgt.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.util.ParameterCheck;
import com.guhecloud.cube.iot.mgt.core.data.lang.ShareDataTypeEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.ShareModeEnum;
import com.guhecloud.cube.iot.mgt.device.mapper.DevDataShareMapper;
import com.guhecloud.cube.iot.mgt.device.mapper.DevDeviceMapper;
import com.guhecloud.cube.iot.mgt.device.model.DevDataShare;
import com.guhecloud.cube.iot.mgt.device.model.DevDevice;
import com.guhecloud.cube.iot.mgt.device.param.DevDataShareBatchDelParam;
import com.guhecloud.cube.iot.mgt.device.param.DevDataShareCreateParam;
import com.guhecloud.cube.iot.mgt.device.param.DevDataShareUpdateParam;
import com.guhecloud.cube.iot.mgt.device.service.DevDataShareService;
import com.guhecloud.cube.iot.mgt.device.vo.DataShareCfgEventChange;
import com.guhecloud.cube.iot.mgt.device.vo.DevDataShareSummaryVo;
import com.guhecloud.cube.iot.mgt.device.vo.DevDataShareVo;
import com.guhecloud.cube.iot.mgt.north.server.tx.TransactionAfterCommitUtils;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductEventMapper;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductMapper;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductPropMapper;
import com.guhecloud.cube.iot.mgt.things.model.MdProduct;
import com.guhecloud.cube.iot.mgt.things.model.MdProductEvent;
import com.guhecloud.cube.iot.mgt.things.model.MdProductProp;
import com.guhecloud.cube.iot.mgt.things.vo.EventConfig;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.TriggerType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DevDataShareServiceImpl extends ServiceImpl<DevDataShareMapper, DevDataShare> implements DevDataShareService {

    private static final String DATASHARE_EVENT_CHANNEL = "iot:v2:datashare:change";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Resource
    private PlatformEvnCom platformEvnCom;
    @Resource
    private DevDeviceMapper devDeviceMapper;
    @Resource
    private MdProductMapper mdProductMapper;
    @Resource
    private MdProductPropMapper mdProductPropMapper;
    @Resource
    private MdProductEventMapper mdProductEventMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void create(DevDataShareCreateParam param) {
        ParameterCheck.notNull(param, "参数不能为空");
        validateParam(param);

        DevDataShare entity = buildEntity(param, platformEvnCom.nextSnowflakeId(), null);
        assertNotDuplicate(entity, null);
        this.save(entity);

        // 发送新增事件通知
        publishDataShareChangeEvent("ADD", entity.getId(), entity.getSourceDeviceId());
    }

    @Override
    public void update(DevDataShareUpdateParam param) {
        ParameterCheck.notNull(param, "参数不能为空");
        ParameterCheck.notNull(param.getId(), "ID不能为空");
        validateParam(param);

        DevDataShare db = this.getById(param.getId());
        ParameterCheck.notNull(db, "共享配置不存在");

        DevDataShare entity = buildEntity(param, param.getId(), db.getExtendConfig());
        assertNotDuplicate(entity, param.getId());
        this.updateById(entity);

        // 发送更新事件通知
        publishDataShareChangeEvent("UPDATE", entity.getId(), entity.getSourceDeviceId());
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        DevDataShare db = this.getById(id);
        if (db != null) {
            this.removeById(id);
            // 发送删除事件通知
            publishDataShareChangeEvent("DELETE", id, db.getSourceDeviceId());
        }
    }

    @Override
    @Transactional
    public void batchDelete(DevDataShareBatchDelParam param) {
        if (param == null || CollectionUtils.isEmpty(param.getIds())) {
            return;
        }

        // 先查询出所有记录，用于后续发送通知
        List<DevDataShare> toDelete = this.listByIds(param.getIds());

        this.removeBatchByIds(param.getIds());

        // 为每条记录发送删除事件通知
        for (DevDataShare entity : toDelete) {
            publishDataShareChangeEvent("DELETE", entity.getId(), entity.getSourceDeviceId());
        }
    }

    @Override
    @Transactional
    public void batchCreate(List<DevDataShareCreateParam> items) {
        // 1. 参数校验
        if (CollectionUtils.isEmpty(items)) {
            return;
        }

        List<String> errorMsgList = new ArrayList<>();
        List<DevDataShare> entitiesToSave = new ArrayList<>();

        // 2. 逐条验证并构建实体
        for (int i = 0; i < items.size(); i++) {
            DevDataShareCreateParam item = items.get(i);
            try {
                // 2.1 参数验证
                ParameterCheck.notNull(item, String.format("第%d条记录参数不能为空", i + 1));
                validateParam(item);

                // 2.2 构建实体
                Long id = platformEvnCom.nextSnowflakeId();
                DevDataShare entity = buildEntity(item, id, null);

                // 2.3 重复检查
                assertNotDuplicate(entity, null);

                entitiesToSave.add(entity);

            } catch (Exception e) {
                String errorMsg = String.format("第%d条记录创建失败: 源设备[%s] -> 目标设备[%s], 原因: %s",
                        i + 1,
                        item.getSourceDeviceId(),
                        item.getTargetDeviceId(),
                        e.getMessage());
                errorMsgList.add(errorMsg);
                log.warn("批量创建数据共享配置失败: {}", errorMsg, e);
            }
        }

        // 3. 如果有任何错误，抛出异常触发事务回滚
        if (!errorMsgList.isEmpty()) {
            String errorSummary = String.format("批量创建失败，共%d条错误", errorMsgList.size());
            // 只显示前3条错误，避免异常信息过长
            if (errorMsgList.size() <= 3) {
                errorSummary += ": " + String.join("; ", errorMsgList);
            } else {
                errorSummary += ": " + String.join("; ", errorMsgList.subList(0, 3)) + "...";
            }
            throw new ServiceException(errorSummary);
        }

        // 4. 批量保存
        this.saveBatch(entitiesToSave);

        // 5. 发送事件通知（事务提交后）
        for (DevDataShare entity : entitiesToSave) {
            publishDataShareChangeEvent("ADD", entity.getId(), entity.getSourceDeviceId());
        }

        log.info("批量创建数据共享配置成功，共{}条", entitiesToSave.size());
    }

    @Override
    public List<DevDataShareVo> listAsSource(Long deviceId) {
        return list(Direction.SOURCE, deviceId);
    }

    @Override
    public List<DevDataShareVo> listAsTarget(Long deviceId) {
        return list(Direction.TARGET, deviceId);
    }

    private List<DevDataShareVo> list(Direction direction, Long deviceId) {
        if (deviceId == null) {
            return Collections.emptyList();
        }

        List<DevDataShare> list;
        if (direction == Direction.SOURCE) {
            list = this.list(new LambdaQueryWrapper<DevDataShare>()
                    .eq(DevDataShare::getSourceDeviceId, deviceId)
                    .orderByDesc(DevDataShare::getCreateTime));
        } else {
            list = this.list(new LambdaQueryWrapper<DevDataShare>()
                    .eq(DevDataShare::getTargetDeviceId, deviceId)
                    .orderByDesc(DevDataShare::getCreateTime));
        }

        return toVoList(list);
    }

    @Override
    public DevDataShareSummaryVo summary(Long deviceId) {
        DevDataShareSummaryVo vo = new DevDataShareSummaryVo();
        vo.setDeviceId(deviceId);

        List<DevDataShareVo> asSourceList = listAsSource(deviceId);
        List<DevDataShareVo> asTargetList = listAsTarget(deviceId);

        vo.setAsSourceList(asSourceList);
        vo.setAsTargetList(asTargetList);
        vo.setAsSourceCount(asSourceList == null ? 0 : asSourceList.size());
        vo.setAsTargetCount(asTargetList == null ? 0 : asTargetList.size());
        return vo;
    }

    @Override
    public void removeByDeviceId(Long deviceId) {
        if (deviceId == null) {
            return;
        }

        Integer tenant = currentTenant();

        // 先查询出所有相关记录，用于后续发送通知
        List<DevDataShare> asSourceList = this.lambdaQuery()
                .eq(DevDataShare::getSourceDeviceId, deviceId)
                .eq(DevDataShare::getDeleted, 0)
                .eq(tenant != null, DevDataShare::getTenant, tenant)
                .list();

        List<DevDataShare> asTargetList = this.lambdaQuery()
                .eq(DevDataShare::getTargetDeviceId, deviceId)
                .eq(DevDataShare::getDeleted, 0)
                .eq(tenant != null, DevDataShare::getTenant, tenant)
                .list();

        // 软删除：源设备匹配的记录
        this.lambdaUpdate()
                .eq(DevDataShare::getSourceDeviceId, deviceId)
                .eq(DevDataShare::getDeleted, 0)
                .eq(tenant != null, DevDataShare::getTenant, tenant)
                .set(DevDataShare::getDeleted, 1)
                .update();

        // 软删除：目标设备匹配的记录
        this.lambdaUpdate()
                .eq(DevDataShare::getTargetDeviceId, deviceId)
                .eq(DevDataShare::getDeleted, 0)
                .eq(tenant != null, DevDataShare::getTenant, tenant)
                .set(DevDataShare::getDeleted, 1)
                .update();

        // 为每条记录发送删除事件通知
        for (DevDataShare entity : asSourceList) {
            publishDataShareChangeEvent("DELETE", entity.getId(), entity.getSourceDeviceId());
        }
        for (DevDataShare entity : asTargetList) {
            publishDataShareChangeEvent("DELETE", entity.getId(), entity.getSourceDeviceId());
        }
    }

    private void validateParam(DevDataShareCreateParam param) {
        ParameterCheck.notNull(param.getSourceDeviceId(), "源设备ID不能为空");
        ParameterCheck.notNull(param.getSourceDataType(), "数据类型不能为空");
        ParameterCheck.notNull(param.getSourceDataId(), "源数据ID不能为空");
        ParameterCheck.notNull(param.getTargetDeviceId(), "目标设备ID不能为空");
        ParameterCheck.notNull(param.getTargetDataId(), "目标数据ID不能为空");

        try {
            ShareDataTypeEnum.fromCode(param.getSourceDataType());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        try {
            Integer shareMode = param.getShareMode() == null ? ShareModeEnum.SHARE.code : param.getShareMode();
            ShareModeEnum.fromCode(shareMode);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private DevDataShare buildEntity(DevDataShareCreateParam param, Long id, String extendConfig) {
        DevDevice sourceDevice = devDeviceMapper.selectById(param.getSourceDeviceId());
        ParameterCheck.notNull(sourceDevice, "源设备不存在");
        DevDevice targetDevice = devDeviceMapper.selectById(param.getTargetDeviceId());
        ParameterCheck.notNull(targetDevice, "目标设备不存在");

        Long sourceProductId = sourceDevice.getProductId();
        Long targetProductId = targetDevice.getProductId();
        ParameterCheck.notNull(sourceProductId, "源设备产品ID为空");
        ParameterCheck.notNull(targetProductId, "目标设备产品ID为空");

        DevDataShare entity = new DevDataShare();
        entity.setId(id);
        entity.setSourceDeviceId(param.getSourceDeviceId());
        entity.setSourceProductId(sourceProductId);
        entity.setSourceDataType(param.getSourceDataType());
        entity.setSourceDataId(param.getSourceDataId());
        entity.setTargetDeviceId(param.getTargetDeviceId());
        entity.setTargetProductId(targetProductId);
        entity.setTargetDataId(param.getTargetDataId());
        entity.setShareMode(param.getShareMode() == null ? ShareModeEnum.SHARE.code : param.getShareMode());
        entity.setRemark(param.getRemark());
        entity.setExtendConfig(extendConfig);

        fillDataCode(entity, sourceProductId, targetProductId);
        return entity;
    }

    /**
     * 填充数据编码并校验数据类型匹配
     *
     * @param entity          共享配置实体
     * @param sourceProductId 源产品ID
     * @param targetProductId 目标产品ID
     */
    private void fillDataCode(DevDataShare entity, Long sourceProductId, Long targetProductId) {
        ShareDataTypeEnum dataTypeEnum = ShareDataTypeEnum.fromCode(entity.getSourceDataType());
        if (dataTypeEnum == ShareDataTypeEnum.PROPERTY) {
            fillPropertyDataCode(entity, sourceProductId, targetProductId);
            return;
        }

        if (dataTypeEnum == ShareDataTypeEnum.EVENT) {
            fillEventDataCode(entity, sourceProductId, targetProductId);
            return;
        }

        throw new ServiceException("不支持的数据类型: " + entity.getSourceDataType());
    }

    /**
     * 填充属性数据编码并校验数据类型匹配
     */
    private void fillPropertyDataCode(DevDataShare entity, Long sourceProductId, Long targetProductId) {
        MdProductProp sourceProp = mdProductPropMapper.selectById(entity.getSourceDataId());
        ParameterCheck.notNull(sourceProp, "源属性不存在");
        ParameterCheck.isTrue(Objects.equals(sourceProp.getProdId(), sourceProductId), "源属性不属于源设备产品");

        MdProductProp targetProp = mdProductPropMapper.selectById(entity.getTargetDataId());
        ParameterCheck.notNull(targetProp, "目标属性不存在");
        ParameterCheck.isTrue(Objects.equals(targetProp.getProdId(), targetProductId), "目标属性不属于目标设备产品");

        // 校验属性数据类型是否匹配
        validatePropertyDataType(sourceProp, targetProp);

        entity.setSourceDataCode(sourceProp.getPropCode());
        entity.setTargetDataCode(targetProp.getPropCode());
    }

    /**
     * 校验源属性和目标属性的数据类型是否匹配
     */
    private void validatePropertyDataType(MdProductProp sourceProp, MdProductProp targetProp) {
        String sourceDataType = sourceProp.getDataType();
        String targetDataType = targetProp.getDataType();

        if (!Objects.equals(sourceDataType, targetDataType)) {
            throw new ServiceException(String.format(
                    "属性数据类型不匹配: 源属性[%s]类型为[%s], 目标属性[%s]类型为[%s]",
                    sourceProp.getPropCode(), sourceDataType,
                    targetProp.getPropCode(), targetDataType));
        }
    }

    /**
     * 填充事件数据编码并校验事件类型和编码匹配
     */
    private void fillEventDataCode(DevDataShare entity, Long sourceProductId, Long targetProductId) {
        MdProductEvent sourceEvent = mdProductEventMapper.selectById(entity.getSourceDataId());
        ParameterCheck.notNull(sourceEvent, "源事件不存在");
        ParameterCheck.isTrue(Objects.equals(sourceEvent.getProdId(), sourceProductId), "源事件不属于源设备产品");

        MdProductEvent targetEvent = mdProductEventMapper.selectById(entity.getTargetDataId());
        ParameterCheck.notNull(targetEvent, "目标事件不存在");
        ParameterCheck.isTrue(Objects.equals(targetEvent.getProdId(), targetProductId), "目标事件不属于目标设备产品");

        // 校验事件类型和编码是否匹配
        validateEventType(sourceEvent, targetEvent);

        entity.setSourceDataCode(sourceEvent.getEventCode());
        entity.setTargetDataCode(targetEvent.getEventCode());
    }

    /**
     * 校验事件类型和编码是否匹配
     * 仅支持 SIMPLE 类型事件，且 eventCode 必须相等
     */
    private void validateEventType(MdProductEvent sourceEvent, MdProductEvent targetEvent) {
        TriggerType sourceTriggerType = parseEventTriggerType(sourceEvent);
        TriggerType targetTriggerType = parseEventTriggerType(targetEvent);

        // 校验源事件必须是 NATURE 类型
        if (sourceTriggerType != TriggerType.NATURE) {
            throw new ServiceException(String.format(
                    "源事件[%s]类型为[%s], 仅支持设备原生事件共享",
                    sourceEvent.getEventCode(), sourceTriggerType));
        }

        // 校验目标事件必须是 NATURE 类型
        if (targetTriggerType != TriggerType.NATURE) {
            throw new ServiceException(String.format(
                    "目标事件[%s]类型为[%s], 仅支持设备原生事件共享",
                    targetEvent.getEventCode(), targetTriggerType));
        }

        // 校验事件编码必须相等,不再必须相等
       /* if (!Objects.equals(sourceEvent.getEventCode(), targetEvent.getEventCode())) {
            throw new ServiceException(String.format(
                    "事件编码不匹配: 源事件编码[%s], 目标事件编码[%s]",
                    sourceEvent.getEventCode(), targetEvent.getEventCode()));
        }*/
    }

    /**
     * 解析事件的 TriggerType
     */
    private TriggerType parseEventTriggerType(MdProductEvent event) {
        EventConfig eventConfig = event.getConfig();

        try {
            TriggerType triggerType = eventConfig.getTriggerType();
            if (triggerType == null) {
                throw new ServiceException(String.format("事件[%s]未配置triggerType", event.getEventCode()));
            }
            return triggerType;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(String.format("事件[%s]配置解析失败: %s", event.getEventCode(), e.getMessage()));
        }
    }

    private void assertNotDuplicate(DevDataShare entity, Long excludeId) {
        Integer tenant = currentTenant();
        long count = this.lambdaQuery()
                .ne(excludeId != null, DevDataShare::getId, excludeId)
                .eq(DevDataShare::getSourceDeviceId, entity.getSourceDeviceId())
                .eq(DevDataShare::getSourceDataType, entity.getSourceDataType())
                .eq(DevDataShare::getSourceDataId, entity.getSourceDataId())
                .eq(DevDataShare::getTargetDeviceId, entity.getTargetDeviceId())
                .eq(DevDataShare::getTargetDataId, entity.getTargetDataId())
                .eq(DevDataShare::getDeleted, 0)
                .eq(tenant != null, DevDataShare::getTenant, tenant)
                .count();

        ParameterCheck.isTrue(count == 0, "共享配置已存在");
    }

    private List<DevDataShareVo> toVoList(List<DevDataShare> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        Set<Long> deviceIds = new HashSet<>();
        Set<Long> productIds = new HashSet<>();
        for (DevDataShare s : list) {
            if (s.getSourceDeviceId() != null) {
                deviceIds.add(s.getSourceDeviceId());
            }
            if (s.getTargetDeviceId() != null) {
                deviceIds.add(s.getTargetDeviceId());
            }
            if (s.getSourceProductId() != null) {
                productIds.add(s.getSourceProductId());
            }
            if (s.getTargetProductId() != null) {
                productIds.add(s.getTargetProductId());
            }
        }

        Map<Long, DevDevice> deviceMap = deviceIds.isEmpty()
                ? Collections.emptyMap()
                : devDeviceMapper.selectByIds(deviceIds)
                .stream()
                .collect(Collectors.toMap(DevDevice::getId, v -> v, (a, b) -> a));

        Map<Long, MdProduct> productMap = productIds.isEmpty()
                ? Collections.emptyMap()
                : mdProductMapper.selectByIds(productIds)
                .stream()
                .collect(Collectors.toMap(MdProduct::getId, v -> v, (a, b) -> a));

        return list.stream()
                .map(s -> {
                    DevDataShareVo vo = new DevDataShareVo();
                    vo.setId(s.getId());

                    DevDevice sourceDevice = deviceMap.get(s.getSourceDeviceId());
                    MdProduct sourceProduct = productMap.get(s.getSourceProductId());
                    DevDevice targetDevice = deviceMap.get(s.getTargetDeviceId());
                    MdProduct targetProduct = productMap.get(s.getTargetProductId());

                    vo.setSourceDeviceId(s.getSourceDeviceId());
                    vo.setSourceDeviceName(sourceDevice == null ? null : sourceDevice.getName());
                    vo.setSourceProductId(s.getSourceProductId());
                    vo.setSourceProductName(sourceProduct == null ? null : sourceProduct.getProdName());
                    vo.setSourceDataType(s.getSourceDataType());
                    vo.setSourceDataId(s.getSourceDataId());
                    vo.setSourceDataCode(s.getSourceDataCode());

                    vo.setTargetDeviceId(s.getTargetDeviceId());
                    vo.setTargetDeviceName(targetDevice == null ? null : targetDevice.getName());
                    vo.setTargetProductId(s.getTargetProductId());
                    vo.setTargetProductName(targetProduct == null ? null : targetProduct.getProdName());
                    vo.setTargetDataId(s.getTargetDataId());
                    vo.setTargetDataCode(s.getTargetDataCode());

                    vo.setShareMode(s.getShareMode());
                    vo.setRemark(s.getRemark());
                    vo.setCreateTime(s.getCreateTime());
                    return vo;
                }).collect(Collectors.toList());
    }

    private Integer currentTenant() {
        Requester requester = SessionContextHolder.getRequester();
        return requester == null ? null : requester.getTenant();
    }

    /**
     * 发送数据共享配置变更事件到 Redis 通道
     *
     * @param eventType      事件类型：ADD、UPDATE、DELETE
     * @param ruleId         共享规则ID
     * @param sourceDeviceId 源设备ID（可选）
     */
    private void publishDataShareChangeEvent(String eventType, Long ruleId, Long sourceDeviceId) {
        TransactionAfterCommitUtils.executeAfterCommit(() -> {
            try {
                DataShareCfgEventChange event = new DataShareCfgEventChange();
                event.setEventType(eventType);
                event.setRuleId(ruleId);
                event.setSourceDeviceId(sourceDeviceId);
                event.setTimestamp(System.currentTimeMillis());

                String json = OBJECT_MAPPER.writeValueAsString(event);

                redisTemplate.convertAndSend(DATASHARE_EVENT_CHANNEL, json);
                log.info("发送数据共享配置变更事件：{}", event);
            } catch (Exception e) {
                log.error("发送数据共享配置变更事件失败：eventType={}, ruleId={}, sourceDeviceId={}",
                        eventType, ruleId, sourceDeviceId, e);
            }
        });
    }

    enum Direction {
        SOURCE,
        TARGET
    }
}
