package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTaskDataModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectDepStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskSplitParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.DetailRateResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskObjCountResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectAbnormalOverviewVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskDepartmentCompletionStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskDepartmentMemberCompletionStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskStatusStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskTypeDistributionVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.PerformInspectionVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskDepStatistics;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

public interface TaskService extends IService<InspectTaskPO> {
    TaskDetailResult getDetail(Long taskId, Boolean notTarget);

    List<StandardResult> getDeailStandardList(Long taskId, Long targetId);

    DetailRateResult getDetailRateResult(TaskPageParam param);

    /**
     * 巡检任务详情巡检项列表导出
     *
     * @param param 任务及对象筛选条件
     *
     * @return 待导出的巡检项列表
     */
    List<InspectRecordVO> getDetailRateExportList(TaskPageParam param);

    Pagination<TaskDetailResult> getPage(TaskPageParam param);

    /**
     * 巡检任务导出分批查询
     *
     * @param param 查询条件（含分页参数）
     *
     * @return 当前批次任务列表
     */
    List<TaskDetailResult> getExportPage(TaskPageParam param);

    void splitTask(TaskSplitParam taskSplitParam);

    String createTaskByPlan(PlanPO planPO);

    String createTaskByPlan(PlanPO po, List<PlanRelationPO> planRelationPOList);

    TaskDetailResult appTaskDetail(Long id);

    PerformInspectionVO performInspection(Long taskId, Long objId);

    List<Long> getTaskTenantList();

    void checkTaskGenerateSendMsg(InspectTaskPO taskPO);

    List<InspectTaskDataModel> getSafetyManagementDataModel(Period period);

    TaskObjCountResult getTaskObjCount(Long taskId);

    Pagination<InspectionObjVO> getInspectionObjInfo(TaskPageParam param);

    InspectionTaskStatisticsVO getInspectionTaskNumStatistics(Period period);

    List<TaskDepStatistics> inspectionDepTaskNumStatistics(InspectDepStatisticsParam param);

    Double getTaskRecordRate(Long taskId, Integer targetNum, Boolean isSplit);

    Float inspectionTaskFinishedRatio(Period period, InspectTaskStatusEnum statusEnum);

    InspectTaskStatusStatVO taskStatusStat(InspectionCommonParam param);

    InspectAbnormalOverviewVO abnormalOverview(InspectionCommonParam param);

    List<InspectTaskTypeDistributionVO> taskTypeDistribution(InspectionCommonParam param);

    List<InspectTaskDepartmentCompletionStatVO> taskDepartmentCompletionStat(InspectionCommonParam param);

    List<InspectTaskDepartmentMemberCompletionStatVO> taskDepartmentMemberCompletionStat(Long deptId, InspectionCommonParam param);

    Float inspectionTaskFinishedRatioPermission(Period period, InspectTaskStatusEnum statusEnum);

    /**
     * 新增巡检任务
     *
     * @param taskParam
     */
    void saveInspectTask(InspectTaskParam taskParam);

    Long getTaskTotal(InspectionCommonParam param);

    /**
     * 巡检处理效率指标
     *
     * @param period
     *
     * @return
     */
    HandlerRateMetricDTO getInspectTaskCompleteRateMetric(Period period);

    /**
     * 巡检处理率指标
     *
     * @param period
     *
     * @return
     */
    HandlerEfficiencyMetricDTO getInspectTaskCompleteEfficiencyMetric(Period period);

    /**
     * 处理数和总处理时长
     *
     * @param period
     *
     * @return
     */
    TaskHandlerTimeVO getInspectTaskTimeCompleteTimeNumMetric(Period period);
}
