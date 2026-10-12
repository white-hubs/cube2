package com.guhecloud.cube.biz.emergency.server.time.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.time.dao.EmergencyRuleWriterEventMapper;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleWriterEventLogPO;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleWriterEventPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleWriterEventService extends ServiceImpl<EmergencyRuleWriterEventMapper, EmergencyRuleWriterEventPO> {

    private final EmergencyRuleWriterEventLogService emergencyRuleWriterEventLogService;

    public List<EmergencyRuleWriterEventPO> getFailWriterEvents(Integer maxRetryCount) {
        return this.baseMapper.selectList(Wrappers.<EmergencyRuleWriterEventPO>lambdaQuery()
                                                  .lt(EmergencyRuleWriterEventPO::getRetryCount, maxRetryCount)
                                                  .and(q -> q.isNotNull(EmergencyRuleWriterEventPO::getThresholdValue)
                                                          .or()
                                                          .ne(EmergencyRuleWriterEventPO::getThresholdValue, ""))
                                                  .and(q -> q.ne(EmergencyRuleWriterEventPO::getWriteStatus, "SUCCESS")
                                                          .or()
                                                          .isNull(EmergencyRuleWriterEventPO::getWriteStatus))
                                                  .and(q -> q.ge(EmergencyRuleWriterEventPO::getEndTime, DateUtils.now())
                                                          .or()
                                                          .isNull(EmergencyRuleWriterEventPO::getEndTime)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateWriterStatus(@NonNull Long id, @NonNull String status) {
        EmergencyRuleWriterEventLogPO emergencyRuleWriterEventLogPO = this.emergencyRuleWriterEventLogService.getById(id);
        if (Objects.isNull(emergencyRuleWriterEventLogPO)) {
            return;
        }
        SessionContextHolder.requestTenant(emergencyRuleWriterEventLogPO.getTenant());
        emergencyRuleWriterEventLogPO.setIotWriteStatus(status);
        EmergencyRuleWriterEventPO emergencyRuleWriterEventPO = this.baseMapper.selectById(emergencyRuleWriterEventLogPO.getEmergencyRuleWriterEventId());
        emergencyRuleWriterEventPO.setWriteStatus(status);
        this.emergencyRuleWriterEventLogService.updateById(emergencyRuleWriterEventLogPO);
        this.baseMapper.updateById(emergencyRuleWriterEventPO);
        SessionContextHolder.clearSessionContext();
    }
}
