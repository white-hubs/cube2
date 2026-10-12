package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskTargetRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.TaskTargetRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectAbnormalOverviewVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectGeneratedTaskTargetVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskDepartmentCompletionStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskDepartmentMemberCompletionStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskStatusStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskTypeDistributionVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 巡检可视化统计
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectVisualizationStatisticsController {

    private final TaskService taskService;
    private final TaskTargetRelationService taskTargetRelationService;

    /**
     * 巡检任务状态统计
     *
     * @param param period 时间范围
     *
     * @return 巡检任务状态统计
     */
    @GetMapping("/inspect/task/status/stat")
    public InspectTaskStatusStatVO taskStatusStat(InspectionCommonParam param) {
        Assert.isParameterError(param == null || param.getPeriod() == null, "统计时间范围不允许为空!");
        return taskService.taskStatusStat(param);
    }

    /**
     * 异常概览统计。
     *
     * @param param period 时间范围
     *
     * @return 异常任务数、异常巡检点数及环比
     */
    @GetMapping("/inspect/abnormal/overview")
    public InspectAbnormalOverviewVO abnormalOverview(InspectionCommonParam param) {
        Assert.isParameterError(param == null, "统计参数不能为空!");
        return taskService.abnormalOverview(param);
    }

    /**
     * 巡检类型分布统计。
     *
     * @param param period 时间范围
     *
     * @return 按巡检类型统计的任务数
     */
    @GetMapping("/inspect/task/type/distribution")
    public List<InspectTaskTypeDistributionVO> taskTypeDistribution(InspectionCommonParam param) {
        Assert.isParameterError(param == null, "统计参数不能为空!");
        return taskService.taskTypeDistribution(param);
    }

    /**
     * 部门维度巡检任务完成统计。
     *
     * @param param period 时间范围
     *
     * @return 按接收部门统计的任务总数和已完成数
     */
    @GetMapping("/inspect/task/department/completion/stat")
    public List<InspectTaskDepartmentCompletionStatVO> taskDepartmentCompletionStat(InspectionCommonParam param) {
        Assert.isParameterError(param == null, "统计参数不能为空!");
        return taskService.taskDepartmentCompletionStat(param);
    }

    /**
     * 部门成员维度巡检任务完成统计。
     *
     * @param deptId 部门 id
     * @param param  period 时间范围
     *
     * @return 按部门成员统计的任务总数、已完成数和完成率
     */
    @GetMapping("/inspect/task/department/completion/member/stat")
    public List<InspectTaskDepartmentMemberCompletionStatVO> taskDepartmentMemberCompletionStat(@RequestParam("deptId") Long deptId,
                                                                                                InspectionCommonParam param
                                                                                               ) {
        Assert.isParameterError(param == null, "统计参数不能为空!");
        return taskService.taskDepartmentMemberCompletionStat(deptId, param);
    }

    /**
     * 查询已生成任务关联过的巡检对象列表。
     *
     * @param targetCategory 巡检对象类型
     *
     * @return 巡检对象列表
     */
    @GetMapping("/inspect/task/target/generated/list")
    public List<InspectGeneratedTaskTargetVO> listGeneratedTargets(@RequestParam("targetCategory") String targetCategory) {
        return taskTargetRelationService.listGeneratedTargets(targetCategory);
    }

    /**
     * 查询最新一次巡检结果为异常的对象列表。
     *
     * @param targetCategory 巡检对象类型
     *
     * @return 巡检对象列表
     */
    @GetMapping("/inspect/task/target/latest-abnormal/list")
    public List<InspectGeneratedTaskTargetVO> listLatestAbnormalTargets(@RequestParam(value = "targetCategory", required = false) String targetCategory) {
        return taskTargetRelationService.listLatestAbnormalTargets(targetCategory);
    }

    /**
     * 查询任务巡检对象列表
     *
     * @param taskId 任务ID
     *
     * @return 巡检对象列表
     */
    @GetMapping("/inspect/task/target/list")
    public List<TaskTargetRelationPO> getTaskTargets(@RequestParam("taskId") Long taskId) {
        Assert.isParameterError(taskId == null, "任务ID不能为空!");
        return taskTargetRelationService.getObjInfo(taskId);
    }

}
