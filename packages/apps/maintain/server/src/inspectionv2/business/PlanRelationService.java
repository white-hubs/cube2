package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanDetailParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionStandardVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 计划关联关系
 */
public interface PlanRelationService extends IService<PlanRelationPO> {
    Pagination<List<InspectTargetInfoDTO>> getTargetInfoByPlanId(PlanDetailParam param);

    //    List<List<InspectTargetInfoDTO>> getTypeTarget(Long planId);

    Pagination<InspectTargetInfoDTO> selectPlanTargetPage(PlanPageParam param);

    List<StandardResult> getStandardList(Long plainId, Long targetId);

    List<InspectionStandardVO> getObjStandardList(Long planId, Long objId);

    PlanRelationPO getObjInfo(Long planId, Long objId);
}
