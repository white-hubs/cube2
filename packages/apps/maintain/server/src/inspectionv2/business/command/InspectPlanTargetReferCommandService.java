package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectPlanTargetReferCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanTargetReferPO;

import java.util.List;

public interface InspectPlanTargetReferCommandService extends IService<InspectPlanTargetReferPO> {

    void saveOrUpdate(Long planId, List<InspectPlanTargetReferCommandModel> commandModels);
}
