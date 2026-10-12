package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.entity.DingTalkSchedule;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.po.DingTalkSchedulePO;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.converters.DingTalkScheduleConvertsPO;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.converters.DingTalkSchedulePOConvertsEntity;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.mapper.DingTalkScheduleMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DingTalkScheduleRepository extends ServiceImpl<DingTalkScheduleMapper, DingTalkSchedulePO> {

    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<DingTalkSchedule> dingTalkSchedules) {
        this.saveBatch(DingTalkScheduleConvertsPO.convertDingTalkScheduleToPOS(dingTalkSchedules), 500);
    }

    public List<DingTalkSchedule> getSchedules(LocalDateTime startTime, LocalDateTime endTime) {
        List<DingTalkSchedulePO> dingTalkSchedulePOS = this.list(Wrappers.<DingTalkSchedulePO>lambdaQuery()
                                                                         .between(DingTalkSchedulePO::getPlanCheckTime, startTime, endTime));
        return DingTalkSchedulePOConvertsEntity.convertDingTalkSchedulePOToEntities(dingTalkSchedulePOS);
    }
}
