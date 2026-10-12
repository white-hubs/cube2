package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionEnableEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.PlanPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.PlanUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionPlanService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.PlanDetailVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.PlanPageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消杀计划接口
 */
@RestController
@RequestMapping("/disinfection/plans")
@RequiredArgsConstructor
@Slf4j
public class BusinessDisinfectionPlanController {
    private final BusinessDisinfectionPlanService businessPlanService;

    /**
     * 新增计划
     */
    @PostMapping
    public Boolean insertPlan(@Valid @RequestBody PlanUpsertParam param) {
        return businessPlanService.insertPlan(param);
    }

    /**
     * 删除计划
     */
    @DeleteMapping("{id}")
    public Boolean deletePlan(@PathVariable Long id) {
        return businessPlanService.deletePlan(id);
    }

    /**
     * 更新计划
     */
    @PutMapping("{id}")
    public Boolean updatePlan(@PathVariable Long id, @Valid @RequestBody PlanUpsertParam param) {
        return businessPlanService.updatePlan(id, param);
    }

    @PutMapping("{id}/{enable}")
    public Boolean updatePlanEnable(@PathVariable Long id, @PathVariable DisinfectionEnableEnum enable) {
        return businessPlanService.updatePlanEnable(id, enable);
    }

    /**
     * 计划详情
     */
    @GetMapping("{id}")
    public PlanDetailVO getPlan(@PathVariable Long id) {
        return businessPlanService.detailPlan(id);
    }

    /**
     * 计划分页列表
     */

    @GetMapping
    public Pagination<PlanPageVO> listPlans(PlanPageParam pageParam) {
        return businessPlanService.pagePlans(pageParam);
    }
}
