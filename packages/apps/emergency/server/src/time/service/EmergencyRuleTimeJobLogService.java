package com.guhecloud.cube.biz.emergency.server.time.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.time.core.eumeration.EmergencyRuleTimeExecuteStatusEnum;
import com.guhecloud.cube.biz.emergency.server.time.dao.EmergencyRuleTimeJobLogMapper;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleTimeJobLogPO;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class EmergencyRuleTimeJobLogService extends ServiceImpl<EmergencyRuleTimeJobLogMapper, EmergencyRuleTimeJobLogPO> {

    public Long saveLog(Long jobId, String openOrClose, LocalDateTime triggerTime) {
        EmergencyRuleTimeJobLogPO emergencyRuleTimeJobLogPO = new EmergencyRuleTimeJobLogPO();
        emergencyRuleTimeJobLogPO.setId(IdWorker.getId());
        emergencyRuleTimeJobLogPO.setOpenOrClose(openOrClose);
        emergencyRuleTimeJobLogPO.setEmergencyRuleTimeJobId(jobId);
        emergencyRuleTimeJobLogPO.setTriggerTime(triggerTime);
        emergencyRuleTimeJobLogPO.setExecuteStatus(EmergencyRuleTimeExecuteStatusEnum.PROCESS_IN);
        this.baseMapper.insert(emergencyRuleTimeJobLogPO);
        return emergencyRuleTimeJobLogPO.getId();
    }

    public void updateLog(Long id, EmergencyRuleTimeExecuteStatusEnum status, String errorMsg) {
        this.update(Wrappers.<EmergencyRuleTimeJobLogPO>lambdaUpdate()
                            .set(EmergencyRuleTimeJobLogPO::getExecuteStatus, status)
                            .set(EmergencyRuleTimeJobLogPO::getHandlerTime, DateUtils.now())
                            .set(EmergencyRuleTimeJobLogPO::getErrorMsg, errorMsg)
                            .eq(EmergencyRuleTimeJobLogPO::getId, id));
    }
}
