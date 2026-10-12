package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionEnableEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.PlanPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.PlanUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.PlanDetailVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.PlanPageVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.validation.Valid;

public interface BusinessDisinfectionPlanService {

    /**
     * 新增计划
     *
     * @param param 插入参数
     *
     * @return 新增计划ID
     */
    boolean insertPlan(@Valid PlanUpsertParam param);

    /**
     * 删除计划
     *
     * @param id 计划ID
     *
     * @return 删除结果
     */
    boolean deletePlan(long id);

    /**
     * 修改计划
     *
     * @param id    计划ID
     * @param param 修改参数
     *
     * @return 修改结果
     */
    boolean updatePlan(long id, PlanUpsertParam param);

    /**
     * 分页
     *
     * @param pageParam 条件
     *
     * @return 分页
     */
    Pagination<PlanPageVO> pagePlans(PlanPageParam pageParam);

    PlanDetailVO detailPlan(Long id);

    boolean updatePlanEnable(long id, DisinfectionEnableEnum enable);
}
