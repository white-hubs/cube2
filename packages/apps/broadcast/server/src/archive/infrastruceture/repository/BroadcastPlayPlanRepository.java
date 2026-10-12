package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates.BroadcastPlayPlan;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastPlayPlanPO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.mapper.BroadcastPlayPlanMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastPlayPlanRepository {

    private final BroadcastPlayPlanMapper broadcastPlayPlanMapper;

    public BroadcastPlayPlan save(BroadcastPlayPlan broadcastPlayPlan) {
        boolean isExecuteSave = true;
        // 如果广播计划已经存在，那么就更新，否则就执行保存
        BroadcastPlayPlanPO po = this.broadcastPlayPlanMapper.selectById(broadcastPlayPlan.getId());
        if (Objects.nonNull(po)) {
            isExecuteSave = false;
        }
        BroadcastPlayPlanPO broadcastPlayPlanPO = BroadcastPlayPlanPO.builder()
                .id(broadcastPlayPlan.getId())
                .startTime(broadcastPlayPlan.getCirculate().getStartTime())
                .endTime(broadcastPlayPlan.getCirculate().getEndTime())
                .playParam(JsonUtils.serialize(broadcastPlayPlan))
                .currentPlayTextTaskId(broadcastPlayPlan.getCurrentPlayTextTaskId())
                .build();
        if (isExecuteSave) {
            this.broadcastPlayPlanMapper.insert(broadcastPlayPlanPO);
        } else {
            broadcastPlayPlanPO.setTenant(po.getTenant());
            broadcastPlayPlanPO.setDeleted(po.getDeleted());
            EntityAuditor.populateCreateAudit(broadcastPlayPlanPO, po.getCreateBy(), po.getCreatorId(),
                                              po.getCreatorType(), po.getCreateTime());
            EntityAuditor.populateUpdateAudit(broadcastPlayPlanPO, po.getUpdateBy(), po.getUpdaterId(),
                                              po.getUpdaterType(), po.getUpdateTime());
            this.broadcastPlayPlanMapper.updateById(broadcastPlayPlanPO);
        }
        if (CollectionUtils.isEmpty(broadcastPlayPlan.getBroadcastPlayText().getDeviceInfos())) {
            DatabaseContextHolder.softDelete(BroadcastPlayPlanPO.class, broadcastPlayPlan.getId());
        }
        return broadcastPlayPlan;
    }

    public List<BroadcastPlayPlan> selectPlayByDeviceId(Long deviceId) {
        List<BroadcastPlayPlanPO> broadcastPlayPlanPOS = this.broadcastPlayPlanMapper.selectPlanByDeviceId(deviceId.toString());
        if (CollectionUtils.isEmpty(broadcastPlayPlanPOS)) {
            return Collections.emptyList();
        }
        List<BroadcastPlayPlan> broadcastPlayPlans = new ArrayList<>(broadcastPlayPlanPOS.size());
        broadcastPlayPlanPOS.forEach(broadcastPlayPlanPO -> {
            broadcastPlayPlans.add(JsonUtils.deserialize(broadcastPlayPlanPO.getPlayParam(), BroadcastPlayPlan.class));
        });
        return broadcastPlayPlans;
    }
}
