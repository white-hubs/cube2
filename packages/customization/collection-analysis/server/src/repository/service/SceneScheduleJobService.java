package com.guhecloud.cube.biz.collection.analysis.repository.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.collection.analysis.repository.mapper.SceneScheduleJobMapper;
import com.guhecloud.cube.biz.collection.analysis.repository.po.SceneScheduleJobPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class SceneScheduleJobService extends ServiceImpl<SceneScheduleJobMapper, SceneScheduleJobPO> {

    public List<SceneScheduleJobPO> getTriggerJobs(long now) {
        return this.baseMapper.selectList(Wrappers.<SceneScheduleJobPO>lambdaQuery()
                                                  .eq(SceneScheduleJobPO::getDeleted, 0L)
                                                  .eq(SceneScheduleJobPO::getEnable, Boolean.TRUE)
                                                  .le(SceneScheduleJobPO::getNextTriggerTime, now)
                                         );
    }

    /**
     * 批量修改下次触发时间
     *
     * @param time  执行时间
     * @param codes 任务编码
     *
     * @return 是否成功
     */
    public boolean updateNextTriggerTime(LocalDateTime time, List<String> codes) {
        if (CollectionUtils.isEmpty(codes) || time == null ||
            time.isBefore(DateUtils.offset(LocalDateTime.now(), ChronoUnit.SECONDS, -10))) {
            return false;
        }
        ZonedDateTime zonedDateTime = time.atZone(ZoneId.systemDefault());
        this.baseMapper.update(Wrappers.<SceneScheduleJobPO>lambdaUpdate()
                                       .set(SceneScheduleJobPO::getNextTriggerTime, zonedDateTime.toInstant().toEpochMilli())
                                       .in(SceneScheduleJobPO::getCode, codes));
        return true;
    }
}
