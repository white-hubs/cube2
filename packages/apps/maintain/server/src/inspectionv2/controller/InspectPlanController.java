package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectPlanCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectPlanQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectPlanCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectPlanQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanAddParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectPlanVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 巡检计划 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectPlanController {

    private final InspectPlanQueryService queryService;
    private final InspectPlanCommandService commandService;

    /**
     * ID查询
     */
    @GetMapping("/inspect-plan/{id}")
    public InspectPlanVO getById(@PathVariable Long id) {
        return queryService.getVOById(id);
    }

    /**
     * 巡检计划-详情-对象的巡检标准列表
     * 根据巡检计划和对象id获取巡检标准列表
     *
     * @return
     */
    @GetMapping("/inspect-record/detail-standard")
    public List<StandardResult> getDeailStandardList(PlanPageParam param) {
        return queryService.getDeailStandardList(param.getPlanId(), param.getTargetId());
    }

    /**
     * 分页
     */
    @GetMapping("/inspect-plan/page")
    public Pagination<InspectPlanVO> getPage(InspectPlanQueryParam param) {
        return queryService.getPage(param);
    }

    /**
     * 新增
     */
    @PostMapping("/inspect-plan")
    public Long save(@RequestBody @Validated InspectPlanCommandModel commandModel) {
        return commandService.save(commandModel);
    }

    /**
     * 巡检记录-新增巡检计划
     *
     * @param param
     */
    @PostMapping("/inspect-plan/add")
    public Long addPlan(@RequestBody @Validated PlanAddParam param) {
        return commandService.addPlan(param);
    }

    /**
     * 开启或关闭计划
     *
     * @param id
     * @param startFlag
     */
    @PutMapping("/inspect-plan/{id}/start/{startFlag}")
    public void getInspectTaskPage(@PathVariable("id") Long id, @PathVariable("startFlag") Boolean startFlag) {
        if (startFlag) {
            commandService.startPlan(id);
        } else {
            commandService.stopPlan(id);
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/inspect-plan/{id}")
    public void deleteInspectResult(@PathVariable Long id) {
        commandService.remove(id);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/inspect-plan/batch")
    public void deleteInspectResult(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        //各模块相关批量删除接口，和前端约定了下，前端传id字符串例如 234454,45454  后台做下相应转换
        commandService.removeBatch(ids);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/inspect-plan/all")
    public void removeAll(@RequestParam(value = "ids", defaultValue = "") String ids) {
        //各模块相关批量删除接口，和前端约定了下，前端传id字符串例如 234454,45454  后台做下相应转换
        commandService.removeAll(ids);
    }

    @GetMapping("/inspect/plan/getName/{id}")
    public String getPlanName(@PathVariable("id") Long id) {
        return queryService.getPlanName(id);
    }

}

