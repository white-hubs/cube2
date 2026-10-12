package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.entity.BroadcastPlayText;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.Circulate;
import lombok.Getter;

/**
 * 广播文本循环播放
 *
 * @author lanchun
 * @date 2023/3/23 11:58
 */
@Getter
public class BroadcastPlayPlan {

    private Long id;

    /**
     * 播放参数
     */
    private BroadcastPlayText broadcastPlayText;

    /**
     * 循环参数
     */
    private Circulate circulate;

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
     * 当前播放任务id
     */
    private String currentPlayTextTaskId;

    public BroadcastPlayPlan() {
    }

    public BroadcastPlayPlan(
            BroadcastPlayText broadcastPlayText,
            Circulate circulate,
            String operatorBy,
            String operatorByName,
            Long operatorByDeptId,
            String operatorByDeptName
                            ) {
        this.id = IdWorker.getId();
        this.broadcastPlayText = broadcastPlayText;
        this.circulate = circulate;
        this.operatorBy = operatorBy;
        this.operatorByName = operatorByName;
        this.operatorByDeptId = operatorByDeptId;
        this.operatorByDeptName = operatorByDeptName;
    }

    public BroadcastPlayPlan(
            Long id,
            BroadcastPlayText broadcastPlayText,
            Circulate circulate,
            String operatorBy,
            String operatorByName,
            Long operatorByDeptId,
            String operatorByDeptName
                            ) {
        this.id = id;
        this.broadcastPlayText = broadcastPlayText;
        this.circulate = circulate;
        this.operatorBy = operatorBy;
        this.operatorByName = operatorByName;
        this.operatorByDeptId = operatorByDeptId;
        this.operatorByDeptName = operatorByDeptName;
    }

    public void settingCurrentPlayTextTask(String currentPlayTextTaskId) {
        this.currentPlayTextTaskId = currentPlayTextTaskId;
    }
}
