package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.entity.BroadcastPlayText;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.BroadcastPlayTextResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BroadcastPlayRecord {

    private Long id;

    /**
     * 播放参数
     */
    private BroadcastPlayText broadcastPlayText;

    /**
     * 广播计划主键
     */
    private Long broadcastPlayPlanId;

    /**
     * 操作人
     */
    private String operatorBy;

    /**
     * 操作人名称
     */
    private String operatorByName;

    /**
     * 操作人部门
     */
    private Long operatorByDeptId;

    /**
     * 操作人部门名称
     */
    private String operatorByDeptName;

    /**
     * 播放结果
     */
    private BroadcastPlayTextResponse broadcastPlayTextResponse;

    public BroadcastPlayRecord(BroadcastPlayText broadcastPlayText,
                               Long broadcastPlayPlanId,
                               String operatorBy,
                               String operatorByName,
                               Long operatorByDeptId,
                               String operatorByDeptName
                              ) {
        this.id = IdWorker.getId();
        this.broadcastPlayText = broadcastPlayText;
        this.broadcastPlayPlanId = broadcastPlayPlanId;
        this.operatorBy = operatorBy;
        this.operatorByName = operatorByName;
        this.operatorByDeptId = operatorByDeptId;
        this.operatorByDeptName = operatorByDeptName;
    }

    /**
     * 设置播放结果状态
     *
     * @param broadcastPlayTextResponse
     */
    public void changeBroadcastPlayStatus(BroadcastPlayTextResponse broadcastPlayTextResponse) {
        this.broadcastPlayTextResponse = broadcastPlayTextResponse;
    }

}
