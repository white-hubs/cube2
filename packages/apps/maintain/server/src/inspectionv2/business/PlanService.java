package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanAddParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanDetailParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanUpdateParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.PlanDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionStandardVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 巡检计划
 */
public interface PlanService extends IService<PlanPO> {
    Pagination<PlanDetailResult> getPage(PlanPageParam param);

    PlanDetailResult getDetail(PlanDetailParam param);

    List<StandardResult> getDeailStandardList(Long plainId, Long targetId);

    Long saveOrUpdatePlan(PlanAddParam param);

    Long editPlanPrecise(@RequestBody @Validated PlanUpdateParam param);

    List<InspectionStandardVO> getStandardList(Long planId, Long objId);

    PlanRelationPO getObjInfo(Long planId, Long objId);

    Boolean checkPlanNameIsRepeat(String planName);

    void saveBatchPlanRelation(List<PlanRelationPO> taskTargetDTOList);

    PlanDetailResult getDetailOnly(Long id);

    Pagination<InspectTargetInfoDTO> getPlanTargetPage(PlanPageParam param);

    void startPlanJob(String jobId);

    void stopPlanJob(String jobId);

    void removePlanJob(String jobId);
}
