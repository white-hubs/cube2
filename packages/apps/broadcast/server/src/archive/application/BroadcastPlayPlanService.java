package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastPlayPlanPO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.mapper.BroadcastPlayPlanMapper;
import com.guhecloud.platform.base.util.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BroadcastPlayPlanService extends ServiceImpl<BroadcastPlayPlanMapper, BroadcastPlayPlanPO> {

    public List<BroadcastPlayPlanPO> findExecutePlayPlan() {
        LocalDateTime executeDate = DateUtils.now();
        return this.baseMapper.selectList(Wrappers.<BroadcastPlayPlanPO>lambdaQuery()
                                                  .le(BroadcastPlayPlanPO::getStartTime, executeDate)
                                                  .ge(BroadcastPlayPlanPO::getEndTime, executeDate)
                                         );
    }
}
