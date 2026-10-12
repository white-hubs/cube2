package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.TaskTargetRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectGeneratedTaskTargetVO;

import java.util.List;

public interface TaskTargetRelationService extends IService<TaskTargetRelationPO> {
    List<List<InspectTargetInfoDTO>> getTargetInfoByTaskId(Long taskId);

    List<TaskTargetRelationPO> getObjInfo(Long taskId);

    List<InspectGeneratedTaskTargetVO> listGeneratedTargets(String targetCategory);

    List<InspectGeneratedTaskTargetVO> listLatestAbnormalTargets(String targetCategory);
}
