package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectTaskParentStatusSyncService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectTaskStatusChangeService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTaskStatusChangeServiceImpl implements InspectTaskStatusChangeService {

    private final InspectTaskMapper taskMapper;
    private final InspectTaskParentStatusSyncService inspectTaskParentStatusSyncService;

    /**
     * 任务生成状态改变
     *
     * @param taskId
     */
    @Override
    public InspectTaskPO changeForTaskAccept(Long taskId) {
        InspectTaskPO po = taskMapper.selectById(taskId);
        po.setStatus(InspectTaskStatusEnum.ACCEPT);
        taskMapper.updateById(po);
        return po;
    }

    /**
     * 任务接受状态改变
     *
     * @param taskId
     */
    @Override
    public InspectTaskPO changeForTaskGenerate(Long taskId) {
        InspectTaskPO po = taskMapper.selectById(taskId);
        po.setStatus(InspectTaskStatusEnum.EXECUTING);
        taskMapper.updateById(po);
        return po;
    }

    /**
     * 任务过期状态改变
     *
     * @param taskId
     */
    @Override
    public InspectTaskPO changeForTaskExpire(Long taskId) {
        InspectTaskPO po = taskMapper.selectById(taskId);
        po.setStatus(InspectTaskStatusEnum.EXPIRE);
        po.setEndTime(LocalDateTime.now());
        taskMapper.updateById(po);
        return po;
    }

    /**
     * 任务处理完成
     *
     * @param taskId
     */
    @Override
    public InspectTaskPO changeForTaskComplete(Long taskId) {
        InspectTaskPO po = taskMapper.selectById(taskId);
        Integer t = taskMapper.countAbnormalTaskNum(Wrappers.query()
                                                            .eq("a.id", taskId)
                                                            .eq("b.result", InspectRecordResultEnum.ABNORMAL.name()));
        po.setResult(t > 0 ? InspectRecordResultEnum.ABNORMAL.name() : InspectRecordResultEnum.NORMAL.name());
        var now = LocalDateTime.now();
        po.setStatus(po.getValidEndTime().isBefore(now) ? InspectTaskStatusEnum.EXPIRE : InspectTaskStatusEnum.OVER);
        po.setEndTime(LocalDateTime.now());
        taskMapper.updateById(po);
        inspectTaskParentStatusSyncService.syncParentTaskStatus(po.getId());
        return po;
    }

    /**
     * 任务终止
     *
     * @param taskId
     */
    @Override
    public InspectTaskPO changeForTaskSTOP(Long taskId) {
        InspectTaskPO po = taskMapper.selectById(taskId);
        po.setStatus(InspectTaskStatusEnum.STOP);
        po.setEndTime(LocalDateTime.now());
        taskMapper.updateById(po);
        return po;
    }
}
