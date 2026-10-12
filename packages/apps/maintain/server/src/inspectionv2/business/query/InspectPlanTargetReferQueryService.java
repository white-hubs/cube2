package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanTargetReferPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectPlanTargetReferVO;

import java.util.List;

public interface InspectPlanTargetReferQueryService extends IService<InspectPlanTargetReferPO> {

    /**
     * 查询巡检任务下的对象
     */
    List<InspectPlanTargetReferVO> getVOByPlanId(Long planId);

    InspectPlanTargetReferVO getVOByPlanIdAndTargetId(Long planId, Long targetId, Long taskId);
}
