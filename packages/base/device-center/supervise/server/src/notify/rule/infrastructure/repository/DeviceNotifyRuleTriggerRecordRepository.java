package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.querygateway.query.DeviceNotifyRuleTriggerRecordQuery;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRuleTriggerRecord;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyRuleTriggerRecordPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter.DeviceNotifyRuleTriggerRecordEntityTOPOConvert;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter.DeviceNotifyRuleTriggerRecordPOTOEntityConvert;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.mapper.DeviceNotifyRuleTriggerRecordMapper;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class DeviceNotifyRuleTriggerRecordRepository {

    private final DeviceNotifyRuleTriggerRecordMapper deviceNotifyRuleTriggerRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long save(DeviceNotifyRuleTriggerRecord triggerRecord) {
        var recordPO = DeviceNotifyRuleTriggerRecordEntityTOPOConvert.deviceNotifyRuleTriggerRecordToPO(triggerRecord);
        deviceNotifyRuleTriggerRecordMapper.insert(recordPO);
        triggerRecord.setId(recordPO.getId());
        return recordPO.getId();
    }

    /**
     * 查询设备通知规则触发记录分页
     *
     * @param query 查询参数
     *
     * @return 设备通知规则触发记录分页
     */
    public Pagination<DeviceNotifyRuleTriggerRecord> findPage(DeviceNotifyRuleTriggerRecordQuery query) {
        var recordPOs = deviceNotifyRuleTriggerRecordMapper.selectPage(PagingUtils.convert(query),
                                                                       Wrappers.<DeviceNotifyRuleTriggerRecordPO>lambdaQuery()
                                                                               .and(StringUtils.isNotBlank(query.getKeyword()), q -> q
                                                                                       .like(DeviceNotifyRuleTriggerRecordPO::getDeviceCode, query.getKeyword())
                                                                                       .or()
                                                                                       .like(DeviceNotifyRuleTriggerRecordPO::getDeviceName, query.getKeyword()))
                                                                               .between(Objects.nonNull(query.getPeriod()), DeviceNotifyRuleTriggerRecordPO::getTriggerTime,
                                                                                        ObjectUtils.ifNull(query.getPeriod(), Period::getBegin), ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                                                                               .orderByDesc(DeviceNotifyRuleTriggerRecordPO::getTriggerTime));
        return Pagination.<DeviceNotifyRuleTriggerRecord>builder()
                .total(recordPOs.getTotal())
                .pages(recordPOs.getPages())
                .records(DeviceNotifyRuleTriggerRecordPOTOEntityConvert.deviceNotifyRuleTriggerRecordToEntities(recordPOs.getRecords()))
                .build();
    }

    /**
     * 根据id查询设备通知规则触发记录
     *
     * @param id 设备通知规则触发记录id
     *
     * @return 设备通知规则触发记录
     */
    public DeviceNotifyRuleTriggerRecord findById(Long id) {
        var recordPO = deviceNotifyRuleTriggerRecordMapper.selectById(id);
        if (Objects.isNull(recordPO)) {
            return null;
        }
        return DeviceNotifyRuleTriggerRecordPOTOEntityConvert.deviceNotifyRuleTriggerRecordToEntity(recordPO);
    }
}
