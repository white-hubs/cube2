package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectionAnalysisService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.BaseInspectStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionAnalysisParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionItemDetailsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.AnalysisOfAuditResultsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepTaskExecutionAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.HomepageStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionFrequencyAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionIObjDetailsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionItemDetailsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjAbnormalAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjTaskAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionPlanAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionRecordAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskUserAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ObjInspectionInfoVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.SafetyReportResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TargetCategoryVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskExecutionAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskTimeAnalysisVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author wql
 * @describe 巡检分析
 * @date 2023/4/25 19:09
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectionAnalysisController {
    private final InspectionAnalysisService inspectionAnalysisService;

    /**
     * @Author wql
     * @Description 巡检计划分析
     * @Date 19:08 2023/4/25
     **/
    @GetMapping("/inspection-analysis/inspectionPlanAnalysis")
    public InspectionPlanAnalysisVO inspectionPlanAnalysis(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.inspectionPlanAnalysis(analysisParam);
    }

    /**
     * @Author wql
     * @Description 巡检任务分析
     * @Date 19:33 2023/4/25
     **/
    @GetMapping("/inspection-analysis/inspectionTaskAnalysis")
    public InspectionTaskAnalysisVO inspectionTaskAnalysis(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.inspectionTaskAnalysis(analysisParam);
    }

    /**
     * @Author wql
     * @Description 巡检次数分析
     * @Date 19:33 2023/4/25
     **/
    @GetMapping("/inspection-analysis/inspectionFrequencyAnalysis")
    public InspectionFrequencyAnalysisVO inspectionFrequencyAnalysis(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.inspectionFrequencyAnalysis(analysisParam);
    }

    /**
     * @Author wql
     * @Description 任务执行分析
     * @Date 19:52 2023/4/25
     **/
    @GetMapping("/inspection-analysis/taskExecutionAnalysis")
    public TaskExecutionAnalysisVO taskExecutionAnalysis(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.taskExecutionAnalysis(analysisParam);
    }

    /**
     * @Author wql
     * @Description 部门巡检任务分析
     * @Date 19:59 2023/4/25
     **/
    @GetMapping("/inspection-analysis/depTaskExecutionAnalysis")
    public DepTaskExecutionAnalysisVO depTaskExecutionAnalysis(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.depTaskExecutionAnalysis(analysisParam);
    }

    /**
     * @Author wql
     * @Description 任务耗时分析
     * @Date 20:06 2023/4/25
     **/
    @GetMapping("/inspection-analysis/taskTimeAnalysis")
    public TaskTimeAnalysisVO taskTimeAnalysis(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.taskTimeAnalysis(analysisParam);
    }

    /**
     * @Author wql
     * @Description 稽查结果分析
     * @Date 20:16 2023/4/25
     **/
    @GetMapping("/inspection-analysis/analysisOfAuditResults")
    public AnalysisOfAuditResultsVO analysisOfAuditResults(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.analysisOfAuditResults(analysisParam);
    }

    /**
     * @Author wql
     * @Description 巡检对象次数分析
     * @Date 20:21 2023/4/25
     **/
    @GetMapping("/inspection-analysis/inspectionObjAnalysis")
    public InspectionObjAnalysisVO inspectionObjAnalysis(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.inspectionObjAnalysis(analysisParam);
    }

    /**
     * @Author wql
     * @Description 巡检对象异常项分析
     * @Date 20:29 2023/4/25
     **/
    @GetMapping("/inspection-analysis/inspectionObjAbnormalAnalysis")
    public InspectionObjAbnormalAnalysisVO inspectionObjAbnormalAnalysis(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.inspectionObjAbnormalAnalysis(analysisParam);
    }

    /**
     * @Author wql
     * @Description 对象巡检任务分析
     * @Date 20:33 2023/4/25
     **/
    @GetMapping("/inspection-analysis/inspectionObjTaskAnalysis")
    public InspectionObjTaskAnalysisVO inspectionObjTaskAnalysis(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.inspectionObjTaskAnalysis(analysisParam);
    }

    /**
     * @Author wql
     * @Description 对象巡检次数top10
     * @Date 20:25 2023/4/18
     **/
    @GetMapping("/inspection-analysis/objInspectionNumTop")
    public List<ObjInspectionInfoVO> objInspectionNumTop(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.objInspectionNumTop(analysisParam);
    }

    /**
     * @Author wql
     * @Description 对象异常项数排名top10
     * @Date 8:48 2023/4/19
     **/
    @GetMapping("/inspection-analysis/objAbnormalNumTop")
    public List<ObjInspectionInfoVO> objAbnormalNumTop(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.objAbnormalNumTop(analysisParam);
    }

    /**
     * @Author wql
     * @Description 对象巡检任务数top10
     * @Date 8:54 2023/4/19
     **/
    @GetMapping("/inspection-analysis/objInspectionTaskTop")
    public List<ObjInspectionInfoVO> objInspectionTaskTop(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.objInspectionTaskTop(analysisParam);
    }

    /**
     * @Author wql
     * @Description 对象巡检任务last10
     * @Date 8:54 2023/4/19
     **/
    @GetMapping("/inspection-analysis/objInspectionTaskLast")
    public List<ObjInspectionInfoVO> objInspectionTaskLast(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.objInspectionTaskLast(analysisParam);
    }

    /**
     * @description: 巡检对象分析
     * @author: lijie
     * @date: 2023/11/14 10:46
     * @param:
     * @return:
     **/
    @GetMapping("/inspection-analysis/getTargetCategory")
    public List<TargetCategoryVO> getTargetCategory(InspectionAnalysisParam analysisParam) {
        return inspectionAnalysisService.getTargetCategory(analysisParam);
    }

    /**
     * 安全巡检情况统计
     *
     * @param period 查询时间
     *
     * @return 巡检统计结果
     */
    @GetMapping("/inspect-analysis/safety-report")
    public SafetyReportResultVO inspectAnalysisSafetyReport(@RequestParam("period") Period period) {
        return inspectionAnalysisService.inspectAnalysisSafetyReport(period);
    }

    /**
     * 对象巡检详情
     */
    @GetMapping("/inspection-analysis/getInspectionStandardTtem")
    public Pagination<InspectionItemDetailsVO> getInspectionStandardTtem(InspectionItemDetailsParam query) {
        return inspectionAnalysisService.getInspectionStandardTtem(query);
    }

    /**
     * 日常巡检统计
     *
     * @param param 查询时间
     *
     * @return 巡检统计结果
     */
    @GetMapping("/inspect-analysis/home-page/statistics")
    public HomepageStatisticsVO inspectAnalysisHomepageStatistics(BaseDateStatisticsParam param) {
        return inspectionAnalysisService.inspectAnalysisHomepageStatistics(param);
    }

    /**
     * 日常巡检-某个对象巡检统计
     *
     * @param param 查询条件
     *
     * @return 巡检统计结果
     */
    @GetMapping("/inspect-analysis/object-details/statistics")
    public InspectionIObjDetailsVO inspectAnalysisObjectDetailsStatistics(InspectionItemDetailsParam param) {
        return inspectionAnalysisService.inspectAnalysisObjectDetailsStatistics(param);
    }

    /**
     * 工程巡查统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect-analysis/task/statistics")
    public InspectionTaskAnalysisVO inspectAnalysisTaskStatistics(BaseDateStatisticsParam param) {
        Assert.isParameterError(Objects.isNull(param.getPeriod()), "时间段不能为空");
        return inspectionAnalysisService.inspectAnalysisTaskStatistics(param);
    }

    /**
     * 工程巡查列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect-analysis/task-execute/user")
    public Pagination<InspectionTaskUserAnalysisVO> inspectAnalysisTaskExecuteUserStatistics(BaseInspectStatisticsParam param) {
        Assert.isParameterError(Objects.isNull(param.getPeriod()), "时间段不能为空");
        return inspectionAnalysisService.inspectAnalysisTaskExecuteUserStatistics(param);
    }

    /**
     * 工程巡查记录
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/inspect-analysis/inspect/record/page")
    public Pagination<InspectionRecordAnalysisVO> inspectAnalysisTaskRecordStatistics(BaseInspectStatisticsParam query) {
        Assert.isParameterError(Objects.isNull(query.getPeriod()), "时间段不能为空");
        return inspectionAnalysisService.inspectAnalysisTaskRecordStatistics(query);
    }

}
