package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.PlanService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanAddParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanDetailParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanUpdateParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.PlanDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 巡检计划控制层
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlanController {

    private final PlanService planService;

    /**
     * 巡检计划-分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect-plan/pageV2")
    @DataPermissionSwitch(dataModel = "inspect:inspectPlan")
    public Pagination<PlanDetailResult> getPage(PlanPageParam param) {
        return planService.getPage(param);
    }

    /**
     * 巡检计划-详情
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect-plan/detailV2")
    public PlanDetailResult getDetail(PlanDetailParam param) {
        return planService.getDetail(param);
    }

    /**
     * @Author wql
     * @Description 校验计划名称是否重复
     * @Date 19:50 2023/4/27
     **/
    @GetMapping("/inspect-plan/checkPlanNameIsRepeat")
    public Boolean checkPlanNameIsRepeat(String planName) {
        return planService.checkPlanNameIsRepeat(planName);
    }

    /**
     * 巡检计划-详情-对象的巡检标准列表
     * 根据巡检计划和对象id获取巡检标准列表
     *
     * @return
     */
    @GetMapping("/inspect-record/detail-standardV2")
    public List<StandardResult> getDeailStandardList(PlanPageParam param) {
        return planService.getDeailStandardList(param.getPlanId(), param.getTargetId());
    }

    /**
     * 巡检计划-新增巡检计划
     *
     * @param param
     */
    @PostMapping("/inspect-record/addV2")
    public Long addPlan(@RequestBody @Validated PlanAddParam param) {
        Assert.isConflict(planService.checkPlanNameIsRepeat(param.getName()), "计划名称重复");
        return planService.saveOrUpdatePlan(param);
    }

    /**
     * 巡检计划-编辑
     *
     * @param param 计划入参
     *
     * @return 计划id
     */
    @PostMapping("/inspect-plan/edit")
    public Long editPlan(@RequestBody @Validated PlanAddParam param) {
        Assert.isParameterError(param.getId() == null, "主键不能为空！");
        return planService.saveOrUpdatePlan(param);
    }

    /**
     * 巡检计划-详情(只包含计划信息无对象信息)
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/inspect-plan/detail-only")
    public PlanDetailResult getDetailOnly(Long id) {
        return planService.getDetailOnly(id);
    }

    /**
     * 巡检计划-内容(巡检对象和巡检内容)
     *
     * @param param planId
     *
     * @return
     */
    @GetMapping("/inspect-plan/plan-target-page")
    public Pagination<InspectTargetInfoDTO> getPlanTargetPage(PlanPageParam param) {
        return planService.getPlanTargetPage(param);
    }

    /**
     * 巡检计划-编辑
     *
     * @param param 计划入参
     *
     * @return 计划id
     */
    @PostMapping("/inspect-plan/editPrecise")
    public Long editPlanPrecise(@RequestBody @Validated PlanUpdateParam param) {
        Assert.isParameterError(param.getId() == null, "主键不能为空！");
        return planService.editPlanPrecise(param);
    }
}
