package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectRecordStatusChangeService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordStatusChangeServiceImpl implements InspectRecordStatusChangeService {

    private final InspectRecordMapper recordMapper;

    /**
     * 任务生成状态改变
     */
    @Override
    public InspectRecordPO changeForRecordGenerate(InspectRecordPO po) {
        po.setStatus(InspectRecordStatusEnum.EXECUTING);
        recordMapper.updateById(po);
        return po;
    }

    /**
     * 任务过期状态改变
     */
    @Override
    public InspectRecordPO changeForRecordExpire(InspectRecordPO po) {
        po.setStatus(InspectRecordStatusEnum.EXPIRE);
        recordMapper.updateById(po);
        return po;
    }

    /**
     * 任务处理完成
     */
    @Override
    public InspectRecordPO changeForRecordComplete(InspectRecordPO po) {
        po.setStatus(InspectRecordStatusEnum.OVER);
        po.setTime(LocalDateTime.now());
        recordMapper.updateById(po);
        return po;
    }

    /**
     * 任务终止
     */
    @Override
    public InspectRecordPO changeForRecordStop(InspectRecordPO po) {
        po.setStatus(InspectRecordStatusEnum.STOP);
        recordMapper.updateById(po);
        return po;
    }
}
