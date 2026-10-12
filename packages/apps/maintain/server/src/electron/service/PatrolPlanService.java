package com.guhecloud.cube.srv.gen.maintain.server.electron.service;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanStatusParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.KeyValueVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.plan.PatrolPlanPageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.plan.PatrolPlanVO;
import com.guhecloud.platform.base.model.Pagination;

import java.time.LocalDateTime;
import java.util.List;

public interface PatrolPlanService {
    /**
     * 查询计划详情
     *
     * @param id 计划id
     *
     * @return 计划详情
     */
    PatrolPlanVO getPatrolPlan(Long id);

    /**
     * 查询计划列表
     *
     * @param param 查询条件
     *
     * @return 计划信息分页
     */
    Pagination<PatrolPlanPageVO> getPalnPage(PatrolPlanPageParam param);

    List<KeyValueVO> getPalnStatus();

    /**
     * 查询用户是否有考勤应用
     *
     * @return 是否有考勤应用
     */
    Boolean attendance();

    /**
     * 停止、启动计划
     *
     * @param param 计划信息
     *
     * @return 新增结果
     */
    Boolean update(PatrolPlanStatusParam param);

    /**
     * 删除计划
     *
     * @param id 计划id
     *
     * @return 删除结果
     */
    Boolean deleteById(Long id);

    /**
     * 新增巡更计划v1.2
     *
     * @param param 计划信息
     *
     * @return id
     */
    Long addPlan(PatrolPlanParam param);

    Boolean updatePlan(PatrolPlanParam param);

    boolean jobGenerateTask(LocalDateTime nowTime);

    /**
     * 是否存在计划名称
     *
     * @param planName 计划名称
     * @param planId   计划id(排除id)
     *
     * @return true表示已经存在
     */
    Boolean existPatrolPlanName(String planName, Long planId);
}
