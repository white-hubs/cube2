package com.guhecloud.cube.biz.emergency.server.time.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.time.dao.EmergencyRuleTimeJobMapper;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleTimeJobPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class EmergencyRuleTimeJobService extends ServiceImpl<EmergencyRuleTimeJobMapper, EmergencyRuleTimeJobPO> {

    public List<EmergencyRuleTimeJobPO> getTriggerNextJobs(LocalDateTime beginTime, LocalDateTime endTime) {
        //查询生效的报警时间
        List<EmergencyRuleTimeJobPO> emergencyRuleTimePOS = this.baseMapper.selectList(Wrappers.<EmergencyRuleTimeJobPO>lambdaQuery()
                                                                                               .ge(EmergencyRuleTimeJobPO::getTriggerNextTime, DateUtils.toTimestamp(beginTime))
                                                                                               .le(EmergencyRuleTimeJobPO::getTriggerNextTime, DateUtils.toTimestamp(endTime))
                                                                                               .orderByAsc(EmergencyRuleTimeJobPO::getTriggerNextTime));
        return emergencyRuleTimePOS;
    }

    public List<EmergencyRuleTimeJobPO> getEmergencyRuleTimeJobs(Collection<Long> emergencyRuleIds) {
        if (CollectionUtils.isEmpty(emergencyRuleIds)) {
            return Collections.emptyList();
        }
        //查询生效的报警时间
        List<EmergencyRuleTimeJobPO> emergencyRuleTimePOS = this.baseMapper.selectList(Wrappers.<EmergencyRuleTimeJobPO>lambdaQuery()
                                                                                               .in(EmergencyRuleTimeJobPO::getEmergencyRuleId, emergencyRuleIds)
                                                                                               .orderByAsc(EmergencyRuleTimeJobPO::getTriggerNextTime));
        return emergencyRuleTimePOS;
    }

    public List<EmergencyRuleTimeJobPO> getTriggerLastJobs(LocalDateTime beginTime, LocalDateTime endTime, String operateType) {
        List<EmergencyRuleTimeJobPO> emergencyRuleTimePOS = this.baseMapper.selectList(Wrappers.<EmergencyRuleTimeJobPO>lambdaQuery()
                                                                                               .eq(EmergencyRuleTimeJobPO::getOperateType, operateType)
                                                                                               .ge(EmergencyRuleTimeJobPO::getTriggerLastTime, DateUtils.toTimestamp(beginTime))
                                                                                               .le(EmergencyRuleTimeJobPO::getTriggerLastTime, DateUtils.toTimestamp(endTime))
                                                                                               .orderByAsc(EmergencyRuleTimeJobPO::getTriggerNextTime));
        return emergencyRuleTimePOS;
    }

    public void removeByEmergencyRuleIds(Collection<Long> emergencyRuleIds) {
        this.baseMapper.delete(Wrappers.<EmergencyRuleTimeJobPO>lambdaQuery()
                                       .in(EmergencyRuleTimeJobPO::getEmergencyRuleId, emergencyRuleIds));
    }
}
